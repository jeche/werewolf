import requests
 
# Specify the url
url = 'http://localhost:8080/werewolf/addUser'
data = "hi"
# This packages the request (it doesn't make it)
request = urllib2.Request(url, data)

# Sends the request and catches the response
response = urllib2.urlopen(request)
 
# Extracts the response
html = response.read()
 
# Print it out
print html 