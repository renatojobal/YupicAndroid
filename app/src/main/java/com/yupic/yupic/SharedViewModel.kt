package com.yupic.yupic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.yupic.yupic.model.*
import com.yupic.yupic.ui.NODE_TYPE_MULTIPLE_CHOICE
import timber.log.Timber


class SharedViewModel(
    private val database: FirebaseFirestore = Firebase.firestore
) : ViewModel() {

    private val _projectsList: MutableLiveData<List<Project>> = MutableLiveData(listOf())
    val projectsList: LiveData<List<Project>> = _projectsList

    /**
     * User
     */
    private val _user: MutableLiveData<User?> = MutableLiveData(null)
    val user: LiveData<User?> = _user
    fun loginUser(user: User) {
        this._user.value = user
        Timber.i("Logged user: ${this._user.value}")
    }

    fun logoutUser() {
        this._user.value = null
    }

    /**
     * Form
     */
    private val _formNodes: MutableLiveData<List<Node>?> = MutableLiveData(null)
    val formNodes: LiveData<List<Node>?> = _formNodes

    private val _categories: MutableLiveData<List<Category>?> = MutableLiveData(null)
    val categories: LiveData<List<Category>?> = _categories

    private val _selectedCategory: MutableLiveData<Category?> = MutableLiveData(null)
    val selectedCategory: LiveData<Category?> = _selectedCategory

    /**
     * Load form data
     */
    private fun loadFormNodes() {
        database.collection("nodes")
            .get()
            .addOnSuccessListener { result ->
                val nodes = mutableListOf<Node>()
                val categories = mutableListOf(
                    Category(
                        percentage = 100.0,
                        title = "Total",
                        thumbnail = "\uD83E\uDD7E"
                    )
                ) // The first category will be used to allocate the total
                _selectedCategory.value = categories[0]
                Timber.i("Got result: ${result.documents}")
                result.documents.forEach { docSnapshot ->

                    val node = Node(
                        key = docSnapshot.id,
                        subtitle = docSnapshot.getString("subtitle") ?: "",
                        response = docSnapshot.getDouble("response"),
                        factor = docSnapshot.getDouble("factor") ?: 0.0,
                        type = docSnapshot.getString("type") ?: "",
                        result = docSnapshot.getDouble("result") ?: 0.0
                    )

                    val categoryRef = docSnapshot.getDocumentReference("category")

                    categoryRef?.get()?.addOnSuccessListener {
                        var category = it.toObject<Category>()
                        category?.key = it.id

                        if (!categories.contains(category)) {
                            category?.let { safeCategory ->
                                categories.add(safeCategory)
                                node.category = safeCategory
                                updateNode(node)
                            }
                        }


                    }?.addOnFailureListener {
                        Timber.e(it, "Error loading category")
                    }

                    // Add options if type is multiple choice
                    if (node.type == NODE_TYPE_MULTIPLE_CHOICE) {
                        val optionsData =
                            docSnapshot.get("options") as ArrayList<HashMap<String, *>>?
                        val options = mutableListOf<Option>()
                        optionsData?.forEach {
                            val dummyOption = Option(it)
                            options.add(dummyOption)
                        }
                        node.options = options
                    }
                    nodes.add(node)
                }

                _formNodes.value = nodes.toList()
                _categories.value = categories.toList()

            }
            .addOnFailureListener {
                Timber.e(it, "Failed getting form questions")
            }
    }


    private fun loadProjectList() {

        database.collection("projects")
            .get()
            .addOnSuccessListener { result ->
                val projects = mutableListOf<Project>()
                result.documents.forEach { docSnapshot ->
                    val targetProject = docSnapshot.toObject<Project>()
                    targetProject?.let {
                        projects.add(targetProject)
                    }

                }
                _projectsList.value = projects

            }
            .addOnFailureListener {
                Timber.e(it, "Error getting the projects")
            }

    }

    /**
     * Method calculate the carbon footprint
     */
    fun calculateFormResult() {

        var totalResult = 0.0

        _formNodes.value?.forEach { node ->
            totalResult += node.calculateResult()

            _categories.value?.forEach { category ->
                if (node.category == category) {
                    Timber.d("Category found")
                    category.categoryCarbonFootprintKg += node.result
                }
            }
        }


        // Set the percentage for the first category: Total
        _categories.value?.get(0)?.categoryCarbonFootprintKg = totalResult

        _categories.value?.forEach { category ->
            category.calculatePortion(totalResult)
        }


        Timber.d("Current user: ${this._user.value}")
        val dummyUser = _user.value?.copy(carbonFootprint = totalResult)
        _user.value = dummyUser
        Timber.i("Carbon footprint calculated: $totalResult")


    }

    /**
     * FUnction called when the user tap on an answer
     */
    fun updateNode(updatedNode: Node) {
        // Ser for the node in the list and update it
        val dummyList = _formNodes.value?.toMutableList()

        dummyList?.replaceAll { realNode ->
            if (realNode.key == updatedNode.key) {
                updatedNode
            } else {
                realNode
            }
        }
        _formNodes.value = dummyList

    }

    init {
        this.loadProjectList()
        this.loadFormNodes()
    }


}