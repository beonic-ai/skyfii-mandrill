# skyfii-mandril

Yet another scala wrapper for the Mandrill V1 API.

It uses [argonaut](http://argonaut.io/) for json encoding/decoding and [dispatch](http://dispatch.databinder.net/Dispatch.html) for 
asynchronous http interaction.

Currently it partially supports the following Mandrill APIs:
  - users
  - messages
  - subaccounts
