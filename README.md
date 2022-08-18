# jenkins-pipeline
Sample pipeline code for CI/CD pipelines in gopaddle using Jenkins

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
