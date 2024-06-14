# API Example

#### Request

```json
[
  {
    "videoId": "jNQXAC9IVRw",
    "timestamp": "00:00:01",
    "title": "testTitle",
    "description": "testDescription"
  }
]
```

#### Response

```json
[
  {
    "videoId": "jNQXAC9IVRw",
    "timestamp": "00:00:01",
    "success": true,
    "result": {
      "provider": "imgur",
      "title": "testTitle",
      "description": "testDescription",
      "imgLink": "imgur uploaded image link",
      "providerData": {
        "clientId": "imgur client id",
        "provider": "imgur",
        "id": "imgur uploaded image id",
        "deletehash": "imgur uploaded image deletehash"
      }
    },
    "error": {
      "code": 0,
      "description": "No error."
    }
  }
]
```
