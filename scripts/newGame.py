import requests
import time
from requests.auth import HTTPBasicAuth
# Clear users
url = 'http://secure-lake-6285.herokuapp.com/admin/test'
print("Clearing user database using and starting a new game: " + url + " credentials are admin, admin")
request = requests.post(url, auth=HTTPBasicAuth('admin', 'admin'))
print("Clearing was: " + request.json()["status"])

# Add atjones
url = 'http://secure-lake-6285.herokuapp.com/addUser'
payload = {'userName': 'atjones', 'id':'atjones', 'firstName':'A', 'lastName':'J', 'hashedPassword':'test1'}
print("Adding the user " + payload['userName'] +" to the database using " + url + " password is: " + payload['hashedPassword'])
request = requests.post(url, data=payload, auth=HTTPBasicAuth('admin', 'admin'))
print("Addition was: " + request.text)
