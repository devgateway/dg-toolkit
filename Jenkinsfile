#!/usr/bin/env groovy
pipeline {
    agent {
        label 'docker'
    }
    environment {
        appTag = env.BRANCH_NAME.replaceAll('[^\\p{Alnum}-]', '_').toLowerCase()
        registry = 'registry.developmentgateway.org'
        image = "${env.registry}/dgtkitapp:${env.appTag}"
    }
    stages {
        stage('Build') {
            steps {
                withEnv(["DOCKER_BUILDKIT=1"]) {
                    sh "docker build --build-arg BRANCH_NAME=${env.appTag} -t ${env.image} ."
                }
                sh "docker push ${env.image}"
            }
        }
    }
}
