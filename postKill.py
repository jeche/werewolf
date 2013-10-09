import requests
from requests.auth import HTTPBasicAuth
 
# Specify the url
url = 'http://secure-lake-6285.herokuapp.com/players/kill'
payload = {'victim': 'jlchen'}
# Makes request
request = requests.post(url, data=payload, auth=HTTPBasicAuth('atjones', 'test1'))
 
# Print out return value
print request.text