# click-tracker

I have implemented this project with my newly acquired skills in App Engine and it uses technologies like Cloud Endpoints, Objectify, DataStore etc.. for any additional informations please contact me at ivica.taseski94@gmail.com.

The only assumption I have made is that the backend should not redirect users directly but send JSON answer with all the redirection informations embedded in it, since by definition RESTful API's should not send back HTML files or redirect users.

## Products
- [App Engine][1]

## Language
- [Java][2]

## APIs
- [Google Cloud Endpoints][3]
- [Google App Engine Maven plugin][4]
- [Objectify][5]

## Setup Instructions

1. Update the value of `application` in `appengine-web.xml` to the app
   ID you have registered in the App Engine admin console and would
   like to use to host your instance of this sample.


1. Run the application with `mvn appengine:devserver`, and ensure it's
   running by visiting your local server's api explorer's address (by
   default [localhost:8080/_ah/api/explorer][5].)

1. Get the client library with

   $ mvnappengine:endpoints_get_client_lib

   It will generate a client library jar file under the
   `target/endpoints-client-libs/<api-name>/target` directory of your
   project, as well as install the artifact into your local maven
   repository.

1. Deploy your application to Google App Engine with

   $ mvn appengine:update

My backend is already deployed on the Google App Engine and here is the URL for the API EXPLORER which you can use to test it https://apis-explorer.appspot.com/apis-explorer/?base=https://arch-1333.appspot.com/_ah/api#p/

All the dependecies are included in the pom.xml. You can build it and deploy it like any other App Engine Maven project

[1]: https://developers.google.com/appengine
[2]: http://java.com/en/
[3]: https://developers.google.com/appengine/docs/java/endpoints/
[4]: https://developers.google.com/appengine/docs/java/tools/maven
[5]: https://localhost:8080/_ah/api/explorer
[6]: https://console.developers.google.com/
