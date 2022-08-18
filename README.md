# jenkins-pipeline
Sample pipeline code for CI/CD pipelines in gopaddle using Jenkins

## Pre-requisite

As a pre-requisite, an application must be deployed in gopaddle. Below flow chart gives the step by step process to be followed before creating a Jenkins pipeline.

![](/assets/images/jenkinspipe.png)

Since we are building a pipeline for an application deployed in gopaddle, we must first initialize and deploy an application in gopaddle before we move on to creating the pipeline in Jenkins.

+ Subscribe to gopaddle - If you do not have a gopaddle subscription yet, subscribe to the [gopaddle portal](https://portal.gopaddle.io/signUp)
+ [Provision K8s in gopaddle](https://help.gopaddle.io/en/articles/3942973-registering-a-cloud-account)
+ [Add a Container Registry](https://help.gopaddle.io/en/articles/3942974-adding-a-docker-registry) - Add a Container registry to gopaddle, to push or pull Docker images
+ Clone the project locally - Clone the GitHub project to be containerized. 
+ Initialize and deploy the project using gopaddle
    + [Download and install gpctl](https://help.gopaddle.io/en/articles/5116592-installing-and-configuring-gopaddle-command-line-utility) - Now, from your local desktop, download and install gpctl command line utility.
	+ [Perform gpctl init](https://help.gopaddle.io/en/articles/5056807-initializing-a-microservice-from-scratch) - Auto-generate the Dockerfile and Kubernetes YAML, build docker images, and deploy the application.
	+ capture the .gp file with the resource IDs - Once the application is onboarded using gopaddle, gpctl init creates a .gp file in the project folder which contains the ***apiToken***, ***containerID***, ***serviceID***, ***applicationID***, ***projectID***, ***releaseID*** and the ***distributionID***. Make a note of these IDs, as we will be using these in the Jenkins pipeline script.

## Creating pipeline in the Jenkins

- select a New Item option in the Jenkins Dashboard.
- Give the Name of the pipeline and select pipeline option in that window and click on **OK** to create the pipeline project.

![](/assets/images/pipeline-create.jpeg)

- select the **Generic Webhook Trigger** option under the **Build Triggers** menu. In the Generic Webhook Trigger window click the **Post content parameters** Add button. Using that add four variables **serviceID**, **buildID**, **status**, **buildVersion**. Add the variable name and expression
```
serviceID --> $.id 
buildID --> $.message.description.buildID 
status --> $.message.description.status
buildVersion --> $.message.description.buildVersion
```

![](/assets/images/buildtriggers-1~2.png)

- Add a token for authenticate the webhook in the token part. and select **Print contributed variables** checkbox also for adding logs to console.

![](/assets/images/buildtriggers-token-1~2.png)

In the Groovy script first we check the status of the build. if the status is **created** then we have to rolling update the application in the gopaddle using API Token.

 **API**:  https://$endPoint/gateway/v1/$(projectID)/application/$(applicationID)
  
  **Method**: PUT
  
  **Payload**: 
  
  ```json
{
   "deploymentTemplateVersion": "draft",
	"serviceGroups": [
		{
			"name": "petclinic",
			"description": "V2VsY29tZSBtZXNzYWdlIHVwZGF0ZWQ=",
			"id": "serviceGroupID",
			"version": "draft",
			"services": [
				{
					"releaseConfig": {
						"buildID": "'"$buildID"'",
						"version": "'"$buildVersion"'"
						
					},
					"id": "'"$serviceID"'",
					"serviceVersion": "draft"
				}
			]
		}
	],
	"updateType": "buildUpdate"
}
```

It will update the build image version in the application in the gopaddle. but before trigger the build in the gopaddle, create the webhook notification using the webhook URL.

Go to gopaddle portal. 

Click on the Notification.

Go to “HTTP Webhooks” tab.

Choose Type as Jenkins and use the below URL to register jenkins webhook notification.

```
http://<Jenkins_Domain>/generic-webhook-trigger/invoke?token=build-complete
```
Go to the alert page create New alert. select the container with **BuildCreate** option. now you can trigger the build to run the pipeline.





