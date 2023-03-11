## 使用Realm

[官方文档](https://www.mongodb.com/docs/realm/sdk/kotlin/)

### 添加依赖

在build.gradle(app)文件中添加

```groovy
plugins {
	...
    id 'io.realm.kotlin'
}
...

dependencies {
	...
    //MongoDB Realm
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4'
    implementation 'io.realm.kotlin:library-base:1.6.1'
}
```

在build.gradle(项目)文件中添加

```groovy
plugins {
    ...
    id 'io.realm.kotlin' version '1.6.1' apply false
}
```

### 定义Object Model

```kotlin
import io.realm.kotlin.types.ObjectId
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class Item() : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId.create()
    var isComplete: Boolean = false
    var summary: String = ""
    var owner_id: String = ""

    constructor(ownerId: String = "") : this() {
        owner_id = ownerId
    }
}
```

### 创建Realm

```kotlin
val config = RealmConfiguration.create(schema = setOf(Item::class))
val realm: Realm = Realm.open(config)
```

### Create, Read, Update, and Delete Objects

```kotlin
//To create a new Item, instantiate an instance of the Item class and add it to the realm in a write transaction block:
realm.writeBlocking {
    copyToRealm(Item().apply {
        summary = "Do the laundry"
        isComplete = false
    })
}

//query
// all items in the realm
val items: RealmResults<Item> = realm.query<Item>().find()
//filter
// items in the realm whose name begins with the letter 'D'
val itemsThatBeginWIthD: RealmResults<Item> =
    realm.query<Item>("summary BEGINSWITH $0", "D")
        .find()
//  todo items that have not been completed yet
val incompleteItems: RealmResults<Item> =
    realm.query<Item>("isComplete == false")
        .find()

//To modify a Todo item, update its properties in a write transaction block:
// change the first item with open status to complete to show that the todo item has been done
realm.writeBlocking {
    findLatest(incompleteItems[0])?.isComplete = true
}

//delete a Todo item by calling mutableRealm.delete() in a write transaction block:
// delete the first item in the realm
realm.writeBlocking {
    val writeTransactionItems = query<Item>().find()
    delete(writeTransactionItems.first())
}
```

### Watch for Changes

下面这个例子监听所有Item objects的变化

```kotlin
// flow.collect() is blocking -- run it in a background context
val job = CoroutineScope(Dispatchers.Default).launch {
    // create a Flow from the Item collection, then add a listener to the Flow
    val itemsFlow = items.asFlow()
    itemsFlow.collect { changes: ResultsChange<Item> ->
        when (changes) {
            // UpdatedResults means this change represents an update/insert/delete operation
            is UpdatedResults -> {
                changes.insertions // indexes of inserted objects
                changes.insertionRanges // ranges of inserted objects
                changes.changes // indexes of modified objects
                changes.changeRanges // ranges of modified objects
                changes.deletions // indexes of deleted objects
                changes.deletionRanges // ranges of deleted objects
                changes.list // the full collection of objects
            }
            else -> {
                // types other than UpdatedResults are not changes -- ignore them
            }
        }
    }
}
```

当我们完成观察后，记得取消job以关闭携程

```kotlin
job.cancel() // cancel the coroutine containing the listener
```

### 关闭Realm

```kotlin
realm.close()
```

### 查看数据库文件软件

[Realm Studio](https://www.mongodb.com/docs/realm/studio/)

## Room VS MongoDB Realm

### Room

Room是一个开源数据库，由谷歌专门为Android应用程序开发，它被设计成一个轻量级的高效且易于使用的传统SQLite数据库,它使用对象关系映射的概念(ORM)映射Java或co-objects到Room数据库表。

优点

+ Integration with the AAC(与Android架构组件的集成)
+ Strong typing(强大键入功能,有助于防止编译时的错误)
+ Easy to use
+ Good performance

缺点

+ Security
+ Cross-platform

### MongoDB Realm

[Realm](https://www.mongodb.com/realm/mobile/database) 是一个专门为移动端设计的数据库。主要特点是性能好，易使用。在移动端 Realm 颇有一些人气，在轻量的场景下使用还是挺适合的。随着 App 的发展，越来越多的本地数据需要和服务器进行同步。如果团队自己开发，需要制定服务器端数据库的格式（和移动端数据库不一样）、服务器端的存储、需要写一个和服务器进行数据同步的程序。对于小团队而言，为了实现这个需求可是不小的工作量。如果 Realm 也提供一个在云上数据库，再提供一个同步的程序。那么数据同步的需求对于中小规模开发者就非常变得非常轻松了。于是 Realm 推出了 Realm Platform，也叫 Realm Cloud。Realm platform 的收费也是按照容量来算钱。

MongoDB 是一个流行的 NoSQL 数据库，主要是在服务器端使用。后来推出了基于云端的 MongoDB Atlas。随着移动互联网的发展，MongoDB 就想着开发一个在移动端的服务，可以把数据方便的同步到 MongoDB Atlas 上。MongoDB 当时发布了一个 iOS 端的 beta SDK。

优点

+ Cross-platform
+ Serverless database
+ Device Sync
+ Query language
+ Security

缺点

+ Learning curve
+ Pricing