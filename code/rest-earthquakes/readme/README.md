# EarthQuakeProject

## About

An application that shows earthquakes data taken from a REST API.

## Classes

### Model

* `Metadata`: contains general data about responses from `earthquake.usgs` API
* `Geometry`: represents geographic coordinates as latitude, longitude and altitude
* `Earthquake`: represents earthquake data (as magnitude, place, etc...)

### Rest

* `RequestMaker`: a father class that create instance of `OKHttpClient` and `ObjectMapper` in order to get data from a
  generic Http request and storing them into a `JsonNode` object and into classes
* `GeometryRequestMaker`: returns a `Geometry` object with coordinates of a given place using `www.here.com` API
* `EarthquakeRequestMaker`: makes http requests to `earthquake.usgs.gov` API and returns a `List<Earthquake>` with
  filtered features by given arguments

### Controller

* `Overview controller`: manages button's actions, scenes, alerts. It also takes user's input data.

## How it works

Every action (associated to buttons or fields) on the user interface is managed by an `OverviewController` method.

The `OverviewController` creates an instance of `EarthquakeRequestMaker` and calls the `getByParams` method obtaining
data as
`List<Earthquake>`.

## Running the application

### Setting API key

1. Go to the [HERE API website](https://developer.here.com/) and generate a FREE geocode API key;
2. Create a `.env` file into THIS directory and add the following line:

```
API_KEY=yourAPIkey
```

### Running

Open the project folder into a java IDE, compile and start the project. We recommend to
use [IntelliJ IDEA](https://www.jetbrains.com/idea/).

# Getting data from REST APIs

### Definitions

An *API* (Application Programming Interface) is a mechanism that allows communication between two software components
using definitions and protocols.

A *REST API* is a data elaboration interface between a web server and a web client. The API receives **requests** and
provides **responses** with information and data.

The *HTTP PROTOCOL* is a communication protocol that uses GET, POST, DELETE methods.

![API](images/API-image.png)

### Application for HTTP request

* **Web Browser**: each web browser can make HTTP requests with GET method;
* **Postman**: an application to set requests and view response data

## HTTP URL

An URL (Uniform Resource Locator) is a char sequence that uniquely identifies an online resource address. An URL is
composed by the following elements:

* **SCHEME**: that identifies wich protocol use to reach resources;
* **HOST**: the name of website domain or subdomain
* **PATH**: the path to reach interested resources
* **QUERY STRING**: contains input data (for example for the API) using key-value couples. It starts with the `?` char.

![URL](images/URL-parts.jpg)

## Make an HTTP request with JAVA

We can create an example java class in order to make an HTTP request and read the response data:

```java
public class RequestExample {
    public static void main(String[] args) {
        //Here we will place the HTTP request
    }
}
```

### Dependency

To make an HTTP request we can use the **okHttp client** importing the following dependency into `pom.xml` file.

```xml

<dependency>
    <groupId>com.squareup.okhttp3</groupId>
    <artifactId>okhttp</artifactId>
    <version>5.0.0-alpha.12</version>
</dependency>
```

The okHttp client is class that allows to build requests and HTTP calls with built-in classes.
First of all we have to create a new instace of okHttp client:

```java
//Creating a new OkHttpClient class to make requests
OkHttpClient client = new OkHttpClient();
```

### Building URL

Now, in order to make a request to the API we have to create the URL.

Note well, you can also use an URL string, however when you have to make a lot of different requests to the same API
with variable query string parameters the `HttpUrl.Builder()` helps you a lot!

```
//URL data
String protocol = "https";
String host = "jsonplaceholder.typicode.com";
String path = "posts";

//Building URL
HttpUrl.Builder urlBuilder = new HttpUrl.Builder();

        urlBuilder.scheme(protocol);
        urlBuilder.host(host);
        urlBuilder.addPathSegment(path);

URL myurl = urlBuilder.build().url();
```

The result is:

```url
https://jsonplaceholder.typicode.com/posts
```

You can also add query string params befor building the url using the `addQueryParameter(String name, String value)`
method. Here is an example:

```
radius = 500;
builder.addQueryParameter("maxradiuskm", String.valueOf(radius);
```

### Building the request

As you know a REST request is composed by some different parts like METHOD, HEADER, BODY...

The okHttpClient helps you to build the request:

```
//Setting the request parameters (defaults to GET method)
Request request = new Request.Builder()
        .url(myurl)
        .build();
```

### Executing the call

When a software tries to execute a call it could have a positive or negative outcome. In order to stop the program when
a call produces a negative result we can use a `try-catch` structure:

```
//Generating a new call to obtain the response
Call call = client.newCall(request);

//Trying to execute the call
try(Response response = call.execute()){
    //Some action if call result is ok...
}
catch(IOException e){
    //Catching errors and throwing runtime exceptions
    throw new RuntimeException(e);
}
```

## Access to response information

### Parsing body

A *JSON* (JavaScript Object Notation) is a data format for information exchange. Using simple word JSON is a string with
a special format composed by elements of key-value pairs.

The `JsonNode` class is a class that stores information as ke-value pairs. The Jackson dependency provides some helpful
tools that parse a JSON string to a `JsonNode` object.

```xml

<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.16.0</version>
</dependency>
```

The Jackson `ObjectMapper` class can parse a JSON string to a `JsonNode` object using the `readTree()` method.

```
// Saving body data before OkHttpClient closes the request
ResponseBody responseBody = response.body();

// Creating a new ObjcetMapper from JACKSON library
ObjectMapper mapper = new ObjectMapper();

// Parsing the body string to JsonNode format (from JACKSON)
JsonNode bodyNode = mapper.readTree(responseBody.string());

//Printing some information from Json
System.out.println("------- Printing first post infos -------");
System.out.println(bodyNode.get(0).get("id").asInt());
System.out.println(bodyNode.get(0).get("title").asText());
System.out.println(bodyNode.get(0).get("body").asText());
```

As you see now you can access to alla JSON information using key values and `get()` method.

### Parsing Json to class

Often Json data have to be used as class field values. The `ObjectMapper` `readValue()` method helps you to put Json
values into corresponding class fields.

The method takes a String in Json format and a class type. The method returns an object that is an instance of the
provided class with fields filled using Json values.

```
JsonNode jsonbody = mapper.readTree(responseBody.string());
Post pst = mapper.readValue(jsonbody.toString(), Post.class);
pst.getId() //Will print the name contained into jsonbody "id" field.
```

## Developers

**Vincenzo Parente**
> Model classes, User Interface, UI Controller

**Fabrizio Tedeschi**
> Rest classes, Data management, Http Requests, UI Controller
