# skyfii-mandrill

Yet another scala wrapper for the Mandrill V1 API.

It uses [argonaut](http://argonaut.io/) for json encoding/decoding and [dispatch](http://dispatch.databinder.net/Dispatch.html) for 
asynchronous http interaction.

Currently it partially supports the following Mandrill APIs:
  - users
  - messages
  - subaccounts

## Getting Started

When using sbt add the following to your build file:

```scala
libraryDependencies += "io.skyfii" %% "skyfii-mandrill" % "0.0.2"
```

### Test your API key

In an SBT console:
```bash
scala>  import scala.concurrent._
scala>  import scala.concurrent.duration._
scala>  import scala.concurrent.ExecutionContext.Implicits.global
scala>  import io.skyfii.mandrill.service._
scala>  val api = new MandrillApiV1("YOUR-API-KEY")
scala>  Await.result(api.ping, 10 seconds)
res1: Either[io.skyfii.mandrill.model.Error,String] = Right("PONG!")
```
