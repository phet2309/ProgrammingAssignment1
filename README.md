# ProgrammingAssignment1
Image classification and text detection application using AWS platform

## Services Used
* AWS S3
* AWS EC2 Instance
* AWS SQS Messaging Service 
* AWS Rekognition

## Architectural Diagram
![ArchDiagram](https://github.com/phet2309/ProgrammingAssignment1/blob/main/Images/fig1.jpeg)

## Steps Performed
- [x] AWS Learner's Lab Setup
- [x] AWS Cli configuration
- [x] IAM policy check
- [x] AWS EC2 instance configuration
- [x] SSH access to EC2 instances
- [x] AWS SQS configuration
- [x] Image recognition Java application
- [x] Text detection java program
- [x] Java application deployment on EC2 instances

## Step-by-step guide
### AWS Learner's Lab Setup 
* Login to AWS Learner's Lab through student account. The screen displays Readme file to display steps for the setup. AWS details shows the AWS credentials for the account.
* The AWS account can be accessed by clicking the red circle. It turns to green when the lab session is active. The toolbar also displays the remaining student credits.

### AWS CLI Setup
* After installing AWS CLI, then credentials and config can be setup using <br />
```aws configure``` <br />
* This asks for the AccessKey, SecertKey and SessionToken. This creates credentials amd config file in ```.aws``` directory. There can be multiple profiles to use different accounts.
* ```export AWS_PROFILE=<profile_name>``` is used to setup environment profile for aws commands.

### IAM Setup
* Navigate to ```IAM -> Roles -> LabRole```. This is the role which was used to configure services
 and their access management. This role includes the below policies which are mainly required. 
    * ```AmazonRekognitionFullAccess```
    * ```AmzonS3FullAccess``` 
    * ```AmazonSQSFullAccess```

### EC2 Instance Configuration
* Navigate to ```Services -> EC2```
* Navigate to ```Instances -> Launch Instances```
* Name the instance, select ```Amazon Linux 2 Kernel 5.10 AMI 2.0.20230307.0 x86_64 HVM gp2```
  as AMI, 64-bit x86 architecture.
* Select the ```t2.micro``` as instance type.
* Select vockey as keypair.
* Allow SSH, HTTP and HTTPS traffic and configure the security group. Then select MyIP to whitelist your IP address for the
 EC2 instance access through SSH, HTTP or HTTPS.
* Select 8 Gib with general purpose SSD as a storage configuration.
* Select launch instance.
<br /> ![AccessManagementEC2]()

### SSH access to the EC2 instances
* As per requirement, the two instances are created.
<br /> ![RunningInstances]()
* Download the ssh key (labsuser.pem)
* Change the labsuser.pem file's permission to read-only. <br />
```chmod 400 labsuser.pem```
* Navigate to the labsuser.pem file's location and run the below command. <br />
```ssh -i labsuser.pem ec2-user@54.145.80.160``` here IPaddress is IPv4 address of EC2 instance.
* Type yes and the EC2 will be accessible from command line.

### AWS SQS configuration
![SQS]() <br />
* Navigate to ```Amazon SQS``` page.
* Select ```Create Queue```.
* Select queue type and provide the queue name. In our case, ```Car.fifo```
* Put in the configuration details for SQS.
* Select encryption and access policy. It can be assigned to the LabRole also.

### JAVA App for image recognition
* Fetch all the images from S3 bucket.
* Find the image labels and confidence score using AWS Rekognition service.
* The images with Car label and confidence score greater than 90% are marked, and their name is pushed to the AWS
 SQS message queue.
* In the end, -1 message is sent as a last message.
* Prepare the JAR file for deployment.

### JAVA App for text detection
* Fetch the messages one by one from AWS SQS queue.
* Run the text detection service using AWS Rekognition and find the detected text.
* If the queue is empty, it waits for the new messages.
* The images with detected text are written in ```ImageText.txt``` file with their respective indexes.
* Prepare the JAR file for deployment.


### Java application deployment on EC2 instances
![messageCount]()
* SSH to the EC2 instances using respective IP addresses.
* Provide AWS configuration using ```aws configure``` command.
* Install openjdk-java-19.0.1 using the command ```wget https://download.oracle.com/java/19/archive/jdk-19.0.1_linux-x64_bin.tar.gz```
* Extract the files using the command ```tar zxvf jdk-19.0.1_linux-x64_bin.tar.gz``` 
* Move the file using ```sudo mv jdk-19.0.1 /usr/share```
* Edit the etc/profile file using ```sudo vim /etc/profile``` in insert mode. Save the file with :wq after changes.
* Add the below details in /etc/profile file.
```export JAVA_HOME=/usr/share/jdk-19.0.1
export PATH=$JAVA_HOME/bin:$PATH
export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
```
* Check the Java version and verify.
* Move the .jar file in EC2 instance using cyberduck.
* Run the command ```java -jar car-recognition-app-0.0.1-SNAPSHOT.jar``` for car recognition app, and run the ```java -jar text-detection-app-0.0.1-SNAPSHOT.jar```
 command to run text detection app. Both app can run in parallel.
![AppsRunningParallel]()

