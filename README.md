# Estates playback stub

This service is responsible for stubbing API#1488 Display in Integration Framework.

---

To run tests on this service use the test script:

```
./run_all_tests.sh
```

---

To run locally using the micro-service provided by the service manager:

```
sm2 --start ESTATES_ALL
```

---

If you want to run your local copy, then stop the frontend ran by the service manager and run your local code by using the following (port number is 8833 but is defaulted to that in build.sbt).

`sbt run`

---

### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").
