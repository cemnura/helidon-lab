# API Resources  
 - [GET api/hero/id/[id]](#get-hero-by-id)  
 - [GET api/hero/heroes](#get-heroes)  
 - [GET api/hero/heroes?startsWith=[query]](#get-heroes-startswith)  
 - [GET api/hero/villains](#get-villains)  
 - [GET api/hero/villains?startsWith=[query]](#get-villains-startswith)  
 - [GET api/hero/id/[id]?getQuotes=true](#get-quotes-for-hero)
 - [GET api/hero/random](#get-random-hero)
 - [GET api/hero/random?getQuotes=true](#get-quotes-for-random-hero)
  
## GET HERO BY ID

Example: Read - GET

http://localhost:8081/api/hero/id/3  
  
Response body:
  
    {
    	"name": "Magneto",
    	"superpowers": [
    		"Flight",
    		"Magnetism"
    	],
    	"villain": true,
    	"id": 3
    }

## GET heroes  
  
Example: Read - GET

http://localhost:8081/api/hero/heroes
  
Response Body:

    [
    	{
    		"name": "Batman",
    		"superpowers": [
    			"Flight",
    			"Martial arts",
    			"Eidetic memory"
    		],
    		"villain": false,
    		"id": 0
    	},
    	{
    		"name": "Superman",
    		"superpowers": [
    			"Flight",
    			"Healing factor",
    			"Eidetic memory",
    			"Superhuman speed",
    			"Superhuman strength"
    		],
    		"villain": false,
    		"id": 1
    	},
    	{
    		"name": "Wolverine",
    		"superpowers": [
    			"Healing factor",
    			"Regeneration"
    		],
    		"villain": false,
    		"id": 2
    	},
    	.
    	.
    	.
    ]
## GET heroes startswith  
  
Example: Read - GET

http://localhost:8081/api/hero/heroes?startsWith=B
  
Response Body:

    [
    	{
    		"name": "Batman",
    		"superpowers": [
    			"Flight",
    			"Martial arts",
    			"Eidetic memory"
    		],
    		"villain": false,
    		"id": 0
    	}
    ]
    
## GET villains  
  
Example: Read - GET

http://localhost:8081/api/hero/villains
  
Response Body:

    [
    	{
    		"name": "Magneto",
    		"superpowers": [
    			"Flight",
    			"Magnetism"
    		],
    		"villain": true,
    		"id": 3
    	}
    ]
    
    
## GET villains startswith  
  
Example: Read - GET

http://localhost:8081/api/hero/villains?startsWith=M
  
Response Body:

    [
        	{
        		"name": "Magneto",
        		"superpowers": [
        			"Flight",
        			"Magnetism"
        		],
        		"villain": true,
        		"id": 3
        	}
    ]    
    
## GET quotes for hero
  
Example: Read - GET

http://localhost:8081/api/hero/id/3?getQuotes=true
  
Response Body:

    {
    	"name": "Magneto",
    	"superpowers": [
    		"Flight",
    		"Magnetism"
    	],
    	"villain": true,
    	"id": 3,
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
            
# GET random hero

Example: Read - GET

http://localhost:8081/api/hero/random

Response Body:

    {
    	"name": "Superman",
    	"superpowers": [
    		"Flight",
    		"Healing factor",
    		"Eidetic memory",
    		"Superhuman speed",
    		"Superhuman strength"
    	],
    	"villain": false,
    	"id": 1
    }
    
        
# GET quotes for random hero

Example: Read - GET

http://localhost:8081/api/hero/random?getQuotes=true

Response Body: 

    Got Superman
    {
    	"name": "Superman",
    	"superpowers": [
    		"Flight",
    		"Healing factor",
    		"Eidetic memory",
    		"Superhuman speed",
    		"Superhuman strength"
    	],
    	"villain": false,
    	"id": 1,
    	"quotes": []
    }
    
    Got Magneto
    {
    	"name": "Magneto",
    	"superpowers": [
    		"Flight",
    		"Magnetism"
    	],
    	"villain": true,
    	"id": 3,
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