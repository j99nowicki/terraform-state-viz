# terraform-state-viz
Visualise Terraform state with Graphviz

## Build and test
    mvn clean package

## Package
    mvn assembly:single

## Run
    cd target
    java -jar terraform-state-viz-jar-with-dependencies.jar

## Create eclipse project
    mvn eclipse:eclipse

Then import project to eclipse
