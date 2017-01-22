# DS_Project
Distributed System Project web service phase(phase 3). 

#### 1. Bootstrap register
  
  - URL - http://localhost:8081/rest/bootstrap/register
  - method - GET
  - Response - 
    - Response Type (REGOK or ERROR)
    - no_ nodes – Number of node entries that are going to be returned by the bootsrap server
        - 0 – request is successful, no nodes in the system
        - 1 or 2 – request is successful, 1 or 2 nodes' contacts will be returned
        - 9999 – failed, there is some error in the command
        - 9998 – failed, already registered to you, unregister first
        - 9997 – failed, registered to another user, try a different IP and port
        - 9996 – failed, can’t register. BS full.
    - node list - Node detail that are gievn from bootstrap server
    - error - error message
    - example - {"responseType":"REGOK","node_No":1,"nodesList":[{"ip":"localhost","port":8081}],"error":"null"}
        
#### 2. Bootstrap unregister

  - URL - http://localhost:8081/rest/bootstrap/unregister
  - method - GET
  - Response - 
    - Response Type (UNROK or ERROR)
    - errorCode – Indicate success or failure.
      - 0 – successful
      - 9999 – error while unregistering. IP and port may not be in the registry orcommand is incorrect.
    - example - {"responseType":"UNROK","errorCode":0}
            
