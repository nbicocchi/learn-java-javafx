# Expense Tracker

This is an app that can be used to track your expenses using a database.
It will show the analytics through useful charts.

To start the database:
```shell
docker compose -f docker-compose.yaml up
```

If it is the first time you start up the database you have to at least create 1 user, in `LoginController.initDataSource()` you will find these lines, uncomment them:

```
//userRepository.deleteAll();
//userRepository.save(new User("user", String.valueOf("user".hashCode())));
```

More than one client can be connected to the same database, even if it is not hosted locally.
It has to be at least hosted in a device connected to the same Wi-Fi network, and then you can connect to it by replacing `App.JDBC_URL` with this string:

```java
private static final String JDBC_URL = "jdbc:postgresql://{YOUR_IP_ADDRESS}:543/jdbc_schema?user=user&password=secret&ssl=false";
```

Remember to replace `{YOUR_IP_ADDRESS}` with your ip address

## Features

* Protection to user input
* Auto refresh the charts every time you change the expenses
* Pie chart that shows in which type of expenses you sped the most
* Area chart that shows the expenses through the year
* Import/export data using .json files
* Multi user support

### Images and icons found in:

[icons8](https://icons8.it/icons)
