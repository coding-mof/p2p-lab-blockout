### Running Maven from Eclipse

- To build and run your application you simply right-click on the class containing the main method in your project view.
Select `"Run as Java Application"`.
- To build and package the application you need to create a new run configuration. Right click on the "org.blockout" project and select run as `"Maven build..."`. Type into the goals field `"clean install"` and click on apply and run. You should find the packaged application under `"blockout-client/target"`.