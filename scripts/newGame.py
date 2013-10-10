import requests
import time
from requests.auth import HTTPBasicAuth
# Clear users
url = 'http://secure-lake-6285.herokuapp.com/admin/test'
print("Clearing user database of users: " + url + " credentials are admin, admin")
request = requests.post(url, auth=HTTPBasicAuth('admin', 'admin'))
print("Clearing was: " + request.json()["status"])

# Add atjones try first time.  Fails because first it has to recreate admin user.
url = 'http://secure-lake-6285.herokuapp.com/addUser'
payload = {'userName': 'atjones', 'id':'atjones', 'firstName':'A', 'lastName':'J', 'hashedPassword':'test1'}
#print("Adding the user " + payload['userName'] +" to the database using " + url + " password is: " + payload['hashedPassword'])
request = requests.post(url, data=payload, auth=HTTPBasicAuth('admin', 'admin'))

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

url = 'http://secure-lake-6285.herokuapp.com/admin/newgametest'
print("Starting a new game: " + url + " credentials are admin, admin")
request = requests.post(url, auth=HTTPBasicAuth('admin', 'admin'))
print("Starting was: " + request.json()["status"])

print("atjones is always the werewolf in tests")

#Get all the alive players
url = 'http://secure-lake-6285.herokuapp.com/players/alive'
print("Making sure everyone is alive at url: " + url)
request = requests.get(url, auth=HTTPBasicAuth('admin', 'admin'))
end = "Additions were: "
for item in request.json():
    end = end + item["id"] + " "
end = end + "all of whom are currently alive."
print(end)

# Vote for jlchen to die.
url = 'http://secure-lake-6285.herokuapp.com/players/vote'
payload = {'victim': 'jlchen', 'id':'sychen', 'firstName':'A', 'lastName':'J', 'hashedPassword':'test1'}
print("Town is voting for " + payload['victim'])
request = requests.post(url, data=payload, auth=HTTPBasicAuth('aablohm', 'test1'))
print("Addition was: " + request.json()["status"])

#time.sleep(60)