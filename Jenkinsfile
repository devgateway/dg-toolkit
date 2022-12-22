#!/usr/bin/env groovy
pipeline {
    agent {
        label 'docker'
    }
    environment {
        appTag = env.BRANCH_NAME.replaceAll('[^\\p{Alnum}-]', '_').toLowerCase()
        registry = '798366298150.dkr.ecr.us-east-1.amazonaws.com'
        image = "${env.registry}/dgtkit/app:${env.appTag}"
    }
    stages {
        stage('Build') {
            steps {
                script {
                    docker.withRegistry("https://798366298150.dkr.ecr.us-east-1.amazonaws.com", "ecr:us-east-1:aws-ecr-credentials-id") {
                        withEnv(["DOCKER_BUILDKIT=1"]) {
                            sh "docker build --build-arg BRANCH_NAME=${env.appTag} -t ${env.image} ."
                        }
                        sh "docker push ${env.image}"
                    }
                }
            }
        }
    }
}
