import requests
from requests.auth import HTTPBasicAuth
 
# Specify the url
url = 'http://secure-lake-6285.herokuapp.com/player/location'
payload = {'lng': '2.0', 'lat': '2.0'}
# Makes request
request = requests.post(url, data=payload, auth=HTTPBasicAuth('atjones', 'test1'))
 
# Print out return value, should return success.
print request.text