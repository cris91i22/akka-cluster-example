# Akka Cluster application

This application is based on http and persistence sub modules. Http is based simple on Akka routes,
and implements a router to send messages to persistence nodes.

Persistence sub module contains all the logic to save the data into databases. Multiple nodes of persistence
can be launched, those nodes with the http nodes make up a cluster.

Http nodes doesn't see each others, but persistence nodes they does.

// Graphic to demonstrate the explanation


**Note.:** It's required first of all launch persistence nodes before launch http nodes.

### Example:

Launch persistence nodes:
```
- project persistence
- runMain com.akka.cluster.persistence.Boot 2551
- runMain com.akka.cluster.persistence.Boot 2552
```

Launch http nodes:
```
- project http
- runMain com.akka.cluster.Main
```

### Tasks to do

- Implement router into http sub module.
- Logs.
- Gatling, stress test.
- Business logic, In which sub module?
- Configure weakly up members and test it.
- Implement mongodb database
