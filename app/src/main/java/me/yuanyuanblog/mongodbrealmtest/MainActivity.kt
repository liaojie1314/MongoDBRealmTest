package me.yuanyuanblog.mongodbrealmtest

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import me.yuanyuanblog.mongodbrealmtest.screen.HomeScreen
import me.yuanyuanblog.mongodbrealmtest.screen.HomeViewModel
import me.yuanyuanblog.mongodbrealmtest.ui.theme.MongoDBRealmTestTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val config = RealmConfiguration.create(schema = setOf(Item::class))
//        val realm: Realm = Realm.open(config)
//        realm.writeBlocking {
//            copyToRealm(Item().apply {
//                summary = "MongoDB Realm Test"
//                isComplete = false
//            })
//        }
//
//        val items: RealmResults<Item> = realm.query<Item>().find()
//        val itemsThatBeginWithD: RealmResults<Item> =
//            realm.query<Item>("summary BEGINWITH $0", "D").find()
//        val incompleteItems: RealmResults<Item> = realm.query<Item>("isComplete==false").find()
//
//        realm.writeBlocking {
//            findLatest(incompleteItems[0])?.isComplete = true
//        }
//
//        realm.writeBlocking {
//            val writeTransactionItems = realm.query<Item>().find()
//            delete(writeTransactionItems.first())
//        }
//
//        val job = CoroutineScope(Dispatchers.Default).launch {
//            val itemFlow = items.asFlow()
//            itemFlow.collect { changes: ResultsChange<Item> ->
//                when (changes) {
//                    is UpdatedResults -> {
//                        changes.insertions // indexes of inserted objects
//                        changes.insertionRanges // ranges of inserted objects
//                        changes.changes // indexes of modified objects
//                        changes.changeRanges // ranges of modified objects
//                        changes.deletions // indexes of deleted objects
//                        changes.deletionRanges // ranges of deleted objects
//                        changes.list // the full collection of objects
//                    }
//                    else -> {
//                        // TODO:
//                    }
//                }
//            }
//        }
//        job.cancel()
//        realm.close()
        setContent {
            MongoDBRealmTestTheme {
                val viewModel: HomeViewModel = hiltViewModel()
                val data by viewModel.data
                HomeScreen(
                    data = data,
                    filtered = viewModel.filtered.value,
                    name = viewModel.name.value,
                    objectId = viewModel.objectId.value,
                    onNameChanged = { viewModel.updateName(name = it) },
                    onObjectIdChanged = { viewModel.updateObjectId(id = it) },
                    onInsertClicked = { viewModel.insertPerson() },
                    onUpdateClicked = { viewModel.updatePerson() },
                    onDeleteClicked = { viewModel.deletePerson() },
                    onFilterClicked = { viewModel.filterData() }
                )
            }
        }
    }
}
