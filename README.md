# 2 Phase Commit Protocol (2PC) multithread using Remote Procedure Call (RPC)

`cd src`

## To Compile
`javac *.java`

## Start by splitting 3 terminals

### Server
`java Server <IP address> <Port Number>`

### Client 5001
`java KeyValueStoreClient <IP address> <Port Number>`, note that port number can only either be 5001, 5002, 5003, 5004, or 5005

### Client 5002
`java KeyValueStoreClient <IP address> <Port Number>`, note that port number can only either be 5001, 5002, 5003, 5004, or 5005

#### Customized abort
Note that the transaction will be aborted when the client is trying to delete a key when it doesn't exist

## Sample Output
In this sample output, I will repopulate in 5001 so you will see in 5002, the prepopulated values will notify you that key already exist.
<img width="1887" alt="Screenshot 2023-11-20 at 4 00 21 PM" src="https://github.com/amandaay/2PhaseCommitProtocol/assets/58647320/857cd34d-cc15-44fa-a939-cdfccdc1edc0">
