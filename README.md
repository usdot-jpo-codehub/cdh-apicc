# ITS CodeHub Constant Contact API (cdh-apicc)

WebApi to interface Constant [Contact System](https://www.constantcontact.com/index.jsp) and register users to an existing email list.
This API uses [Spring Boot](https://spring.io/projects/spring-boot) as base to expose the REST end point used for the registration.

## Usage
Once the application is running on a configured port it is required to submit a POST request providing the email to be registered and a valid Constant Contact list id as payload of the request.

### Request

 - Method: POST
 - Content-Type: application/json
 - Payload (sample)
```json
	{
		"listId":"0987654321",
		"email":"test@email.com"
	}
```

#### cURL
curl 'http://[HOST:PORT]/api/v1/contacts' -i -X POST -H 'Content-Type: application/json' -d '{"listId":"0987654321","email":"test@email.com"}'

### Response
The response object provides a general response information and the actual data is associated with the "result" property.

Resonse sample
```json
	{
		"timestamp": "2019-10-21T18:56:36Z",
		"status": "OK",
		"code": 200,
		"path": "http://localhost",
		"verb": "POST",
		"traceid": "20191021185636913",
		"result": {
			"id": "1234567890",
			"email": "test@email.com",
			"listId": "0987654321"
		}
	}
```

The following status codes are possible to have as part of a successful response.

- 200 : A contact exists but it was not in the email list.
- 201 : A contact did not exists so it was created and included in the email list.
- 200 : A contact exists and it is already in the email list, in this case the result object will be Null.
- 500 : Internal server error will be provided in case of errors.


## Configuration
The API requires two mandatory and one optional environment variable in order to operate.

 - cdhapicc.apiKey: [**Mandaroty**] Constant Contact API Key.
 - cdhapicc.accessToken: [**Mandarory**] Constant Contact Access Token.
 - server.port: [**Optional**] API server port.

## Installation
The API is a Java application and can be executed updating the values of the following command template.

```bash
sh -c java -Djava.security.egd=file:/dev/./urandom -jar /cdh-apicc-1.0.0.jar"
```
It is important to setup the environment variables before to execute the application.

## File Manifest
* src/main : Contains the source code
* src/test : Contains the unit testing code.
* Dockerfile: Docker image definition file


## Development setup
> The API was developed using [Spring Tool Suite 4](https://spring.io/tools/) that is base on [Eclipse](https://www.eclipse.org/ide/)

1. Install and open Spring Tool Suit
2. Configure the required enviroment variables
3. Register the Constant Contact libraries in Maven local repository by running the following commands.
```bash
mvn install:install-file -Dfile=./libs/java-sdk-5.2.3.jar -DgroupId=com.constantcontact -DartifactId=java-sdk -Dversion=5.2.3 -Dpackaging=jar
```
```bash
mvn install:install-file -Dfile=./libs/java-components-5.2.3.jar -DgroupId=com.constantcontact -DartifactId=java-components -Dversion=5.2.3 -Dpackaging=jar
```

4. Debug/Run as Spring Boot application, after this step the application will be running and ready to receive request.

## Docker Support
A [Docker](https://www.docker.com/) image can be build with the next command line.
```bash
  docker build -t cdh-apicc:1.0.0 .
```

The following command replacing the correct values for the environment variable will start a Docker container.
```bash
docker run -p 3003:3003 --rm -e "cdhapicc.accessToken=[ACCESS_TOKEN]" -e "cdhapicc.apiKey=[API_KEY]" -e "server.port=3003" -t -i cdh-apicc:1.0.0
```


## Release History
* 1.0.0
  * Initial version


## Contact information
Joe Doe : X@Y

Distributed under XYZ license. See *LICENSE* for more information

## Contributing
1. Fork it (https://github.com/usdot-jpo-codehub/cdh-apicc/fork)
2. Create your feature branch (git checkout -b feature/fooBar)
3. Commit your changes (git commit -am 'Add some fooBar')
4. Push to the branch (git push origin feature/fooBar)
5. Create a new Pull Request

## Known Bugs
*

## Credits and Acknowledgment
Thank you to the Department of Transportation for funding to develop this project.

## CODE.GOV Registration Info
* __Agency:__ DOT
* __Short Description:__ WebAPI to interface Constant Contact API.
* __Status:__ Beta
* __Tags:__ CodeHub, Constant Contact, DOT, Spring Boot, Java
* __Labor Hours:__
* __Contact Name:__
* __Contact Phone:__
