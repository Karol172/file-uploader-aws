# File uploader
## Description
This is a simple Java app, which provide API for simple file management. Files can be stored on local machine or AWS S3 Bucket.

## Run on local machine

### Requirements
- [OpenJDK 14](https://jdk.java.net/14/)

### Instruction*
1. Build app by running command `./mvnw clean package` on Unix or `mvnw.cmd clean package` in Windows
2. Set environmental variable WORKDIR (path to directory, where files will be stored, default: ./files): `export WORKDIR=<path>` on Unix or `setx WORKDIR "<path>" on Windows
3. Launch app: `java -jar target/file-uploader.jar` on Unix or `java -jar target\file-uploader.jar` on Windows
4. App is working on 8080 TCP port (API description below)
5. Hit Ctrl+C for stopping app

## Run on docker container
### Requirements
- [OpenJDK 14](https://jdk.java.net/14/)
- [Docker](https://docs.docker.com/docker-for-windows/install/)

### Instruction*
1. Build app by running command: `./mvnw clean package` on Unix or `mvnw.cmd clean package` in Windows
2. Build docker image: `docker build -t file-uploader .`
3. Run docker container: `docker run -d -p 8080:8080 --name file-uploader file-uploader`**
4. App is working on 8080 TCP port (API description below)
5. Stop app using command `docker contaner stop file-uploader`

* Files can be stored on AWS bucket. For using this option environmental variables should be set: 
- BUCKET_NAME (name of the existing bucket), 
- SPRING_PROFILE = aws,
- AWS Credentials to AWS user should be saved on your local/docker machine.  
** Using docker path `/files` should be bind with path on native machine. Instead of using command from step 3 use command: `docker run -d -p 8080:8080 -v <native-machine-path>:/files --name file-uploader file-uploader`

## Run on AWS
### Requirements
- [OpenJDK 14](https://jdk.java.net/14/)
- [Docker](https://docs.docker.com/docker-for-windows/install/)
- [AWS CLI](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html)
- [Terraform](https://www.terraform.io/downloads.html)

### Instruction
1. Build app by running command: `./mvnw clean package` on Unix or `mvnw.cmd clean package` in Windows
2. Run commands on path `./terraform/main`: `terraform init` and `terraform apply`

## Short API description

### Upload file
- URL: `/api/file`
- method: POST
- uploaded files should be stored in `file` key
- successful operation status code: 201
- warning: sending file with existing name will cause overwrite existing file
 
### Download file
- URL: `/api/file/{filename}`
- method: GET
- successful operation status code: 200

### Search file
- URL: `/api/file`
- method: GET
- set start phrase in request parameter `phrase`, which searched filename starts
- results are paging (by default size is 20 and page 0), for customizing set desired values on request parameters: `size`, `page`
- results can be sorted by filenames, set request parameter `sort`, for ascending order: `sort=filename,asc` or `sort=filename,desc` for descending order
- successful operation status code: 200

### Delete file
- URL: `/api/file/{filename}`
- method: DELETE
- successful operation status code: 204
