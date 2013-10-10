import requests
from requests.auth import HTTPBasicAuth
 
# Specify the url for starting a newgame, the int value afterwards specifies the game day night frequency in minutes
url = 'http://secure-lake-6285.herokuapp.com/newgame'
payload = {'dayNight': '5'}
# Makes request, only admin admin can start a new game
request = requests.post(url, data=payload, auth=HTTPBasicAuth('admin', 'admin'))
 
# Print out return value, should be success.
print request.text