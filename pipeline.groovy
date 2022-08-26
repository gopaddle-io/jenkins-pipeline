node{
stage('triggerRollingUpdate'){
sh '''
serviceID = "<service_id>"
GP_API_TOKEN = "<gp_api_token>"
endPoint = "<gopaddle_endpoint>"
projectID = "<project_id>"
applicationID = "<application_id>"
serviceName = "<service_name>"


echo $status 
if [ $status = "Created" ]
then
    description=$(curl -H "Authorization: ${GP_API_TOKEN}" https://$endPoint/gateway/v1/${projectID}/build/${buildID} | jq -r '.artifactConfig.codebaseRepo.repoDetails.message')
    echo "Build created successfully"
    curl -X PUT -H "Authorization: ${GP_API_TOKEN}" -d '{
	"deploymentTemplateVersion": "draft",
	"serviceGroups": [
		{
			"name": "'"$serviceName"'",
			"description": "'"$description"'",
			"id": "'"$serviceID"'",
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
}' https://${endPoint}/gateway/v1/${projectID}/application/${applicationID} > resp.txt
    cat resp.txt
else
    echo "Build Failed!!"
fi
'''
}

