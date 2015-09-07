protoc src/main/resources/test_message.proto --java_out=src/main/java/
java -jar wire-compiler-1.8.0.jar --proto_path=src/main/resources/ --java_out=src/main/java/ wire_message.proto