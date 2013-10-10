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
print("Addition was: " + request.json()["status"])

# Add jlchen
url = 'http://secure-lake-6285.herokuapp.com/addUser'
payload = {'userName': 'jlchen', 'id':'jlchen', 'firstName':'A', 'lastName':'J', 'hashedPassword':'test1'}
print("Adding the user " + payload['userName'] +" to the database using " + url + " password is: " + payload['hashedPassword'])
request = requests.post(url, data=payload, auth=HTTPBasicAuth('admin', 'admin'))
print("Addition was: " + request.json()["status"])

# Add aablohm
url = 'http://secure-lake-6285.herokuapp.com/addUser'
payload = {'userName': 'aablohm', 'id':'aablohm', 'firstName':'A', 'lastName':'J', 'hashedPassword':'test1'}
print("Adding the user " + payload['userName'] +" to the database using " + url + " password is: " + payload['hashedPassword'])
request = requests.post(url, data=payload, auth=HTTPBasicAuth('admin', 'admin'))
print("Addition was: " + request.json()["status"])

# Add sychen
url = 'http://secure-lake-6285.herokuapp.com/addUser'
payload = {'userName': 'sychen', 'id':'sychen', 'firstName':'A', 'lastName':'J', 'hashedPassword':'test1'}
print("Adding the user " + payload['userName'] +" to the database using " + url + " password is: " + payload['hashedPassword'])
request = requests.post(url, data=payload, auth=HTTPBasicAuth('admin', 'admin'))
print("Addition was: " + request.json()["status"])

print("atjones is always the werewolf in tests")

url = 'http://secure-lake-6285.herokuapp.com/players/alive'
print("Making sure everyone is alive at url: " + url)
request = requests.post(url, auth=HTTPBasicAuth('admin', 'admin'))
print("Addition was: " + request.json())

time.sleep(60)