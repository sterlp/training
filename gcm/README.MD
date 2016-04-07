# Prurpose

Sample project to describe how Spring Integration could be used to send messages over Google Clound Messaging (GCM) to Android in both directions.

Please understand this code as a Prove of Concept how it could be solved right now. Some possible solutions are tagged, the last one is on the trunk without any gurantie that it is the best.

# Messages
## ACK for send message
```javascript
{"message_type":"ack","from":"<gcmDeviceToken>","message_id":"69c80c45-5f2b-49b4-955b-929b68add39b"}
```
## NACK Message
```javascript
{"message_type":"nack","from":"","message_id":"89c6e6a0-0d8c-491f-9eaf-e1302c81da22","error":"INVALID_JSON","error_description":"InvalidJson: MISSING_REGISTRATIONS : Missing \"to\" field"}
```

## Receipt
```javascript
{"data":{"message_status":"MESSAGE_SENT_TO_DEVICE","device_registration_id":"<gcmDeviceToken>","message_sent_timestamp":"1458563610226","original_message_id":"69c80c45-5f2b-49b4-955b-929b68add39b"},"time_to_live":0,"from":"gcm.googleapis.com","message_id":"dr2:69c80c45-5f2b-49b4-955b-929b68add39b","message_type":"receipt","category":"org.sterl.gcm.android"}
```
## Upstream Message
```javascript
{"data":{"message":"yhb"},"time_to_live":60000,"from":"dUXsJW01LzI:ISWhbrNse...","message_id":"82fb4063-6bb8-4437-a7ef-20bd12971054","category":"org.sterl.gcm.android"}
```

## NACK device deregistered
```javascript
{"message_type":"nack","from":"<gcmDeviceToken>","message_id":"c1febee8-d371-4a9a-9794-3e153cff01de","error":"DEVICE_UNREGISTERED","error_description":""}
```

