# API Resources  
 - [GET quote/[name]](#get-name)  
 - [POST quote/register](#post-register)  
 - [POST quote/append](#post-append)  

  
## GET quote/[name]

Example: READ - GET  
Example: http://localhost:8082/quote/Magneto  
  
Response body:
  
    {
        "name": "Magneto",
        "quotes": [
            {
                "quote": "Peace was never an option"
            },
            {
                "quote": "You are a god among insects. Never let anyone tell you different."
            },
            {
                "quote": "I've been at the mercy of men just following orders. Never again."
            }
        ]
    }

## POST quote/register  
  
Example: Create - POST
http://localhost:8082/quote/register
  
Request body:  
  
    {
        "name": "Joe",
        "quotes": [
            {
                "quote": "Hello"
            },
            {
                "quote": "Mello"
            }
        ]
    }
Response:

    Registered
  
## POST quote/append
  
Example: UPDATE – POST  
http://localhost:8082/quote/append
  
Request body:  
  
    {
        "name": "Joe",
        "quotes": [
            {
                "quote": "Zello"
            }
        ]
    }
Response:

    Success        