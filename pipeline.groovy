node{
stage('triggerRollingUpdateOnStageSaaS'){
sh '''
echo $status
if [ $status = "Created" ]
then
    echo "Build created successfully"
    curl -X PUT -H "Authorization:token eyJhbGciOiJSUzUxMiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJkZW1vQGdvcGFkZGxlLmlvIiwiZXhwIjoxNjYyNjAwMzgzLCJqdGkiOiJjNGE4MGUxZWc0MjA2ZzQ3YTZnOWM2M2cyYWM5YjAyMDgyNDMiLCJpYXQiOjE2MzEwNjQzODMsImlzcyI6ImdvcGFkZGxlIiwic3ViIjoie1widXNlcklEXCI6XCJ1c3JkYWU1MTFjNmVkZTI2ZTQ5MmRlODk5YmU5ZTMwN2QyYjRmZmZcIn0ifQ.c6XnfBuK5zn80S9_qaHVyN3szPYuSXE2yKIAI5OdAF35dk4Z9j-MrBduDaNMkB18ts_8pwDwv58WSOUa_qUr5e-DzqmnpkuEXHtSUmHQrt1gZ846YZu1o9Ke-2iwZwNoUMBjmVLlRBvh5Fu4SdOss7HG28ohAGuK6Kkx9wBJoVgb3uX_H77tbIRvtVt6QGpFqNm9iWlqKgJT4SPl0vJ-3rrJYJrK_mv-_hpIWt9Vswg1iKvVv2KEiYk0ePbVn6bT-oBVEynw4MQVRAp1__3R6EknkmEz7nDHwiKELw4Rj_8gI8Z7SknsQP8H6yMUFZMgXK2nOP9bZ6eEmjBJEL_7rA" -d '{
	"deploymentTemplateVersion": "draft",
	"serviceGroups": [
		{
			"name": "petclinic",
			"description": "V2VsY29tZSBtZXNzYWdlIHVwZGF0ZWQ=",
			"id": "sgd88cd884sg5e3csg4651sg8ef7sg2f5a0a2d66eb",
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
}' https://qa.gopaddle.io/gateway/v1/prj8a079e39ea348e4b37eae6feabbcfc244d22/application/appsa5766791c8e29c4441c9579c78769ee26bac > resp.txt
    cat resp.txt
else
    echo "Build Failed!!"
fi
'''
}
stage('runRegressionTests'){
    sh '''
    echo $status
    sleep 35
    '''
    build job: 'QA-Job'
}
stage('triggerRollingUpdateOnProdSaaS'){
    sh '''    echo $status
if [ $status = "Created" ]
then
    echo "Build created successfully"
    curl -X PUT -H "Authorization:token eyJhbGciOiJSUzUxMiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJkZW1vQGdvcGFkZGxlLmlvIiwiZXhwIjoxNjYyNjAwMzgzLCJqdGkiOiJjNGE4MGUxZWc0MjA2ZzQ3YTZnOWM2M2cyYWM5YjAyMDgyNDMiLCJpYXQiOjE2MzEwNjQzODMsImlzcyI6ImdvcGFkZGxlIiwic3ViIjoie1widXNlcklEXCI6XCJ1c3JkYWU1MTFjNmVkZTI2ZTQ5MmRlODk5YmU5ZTMwN2QyYjRmZmZcIn0ifQ.c6XnfBuK5zn80S9_qaHVyN3szPYuSXE2yKIAI5OdAF35dk4Z9j-MrBduDaNMkB18ts_8pwDwv58WSOUa_qUr5e-DzqmnpkuEXHtSUmHQrt1gZ846YZu1o9Ke-2iwZwNoUMBjmVLlRBvh5Fu4SdOss7HG28ohAGuK6Kkx9wBJoVgb3uX_H77tbIRvtVt6QGpFqNm9iWlqKgJT4SPl0vJ-3rrJYJrK_mv-_hpIWt9Vswg1iKvVv2KEiYk0ePbVn6bT-oBVEynw4MQVRAp1__3R6EknkmEz7nDHwiKELw4Rj_8gI8Z7SknsQP8H6yMUFZMgXK2nOP9bZ6eEmjBJEL_7rA" -d '{
	"deploymentTemplateVersion": "draft",
	"serviceGroups": [
		{
			"name": "petclinic",
			"description": "V2VsY29tZSBtZXNzYWdlIHVwZGF0ZWQ=",
			"id": "sgd88cd884sg5e3csg4651sg8ef7sg2f5a0a2d66eb",
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
}' https://qa.gopaddle.io/gateway/v1/prj8a079e39ea348e4b37eae6feabbcfc244d22/application/appsa550e1e6c3466c4492c9e8fc2c26db7085f5 > resp.txt
    cat resp.txt
else
    echo "Build Failed!!"
fi
    '''
}
}
