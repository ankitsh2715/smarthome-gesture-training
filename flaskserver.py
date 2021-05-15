
import flask
from flask import  render_template, jsonify, request
import time
import os
import base64

app=flask.Flask(__name__)
upload_folder="/Users/ankitsharma/Desktop/uploadVideo"

app.config ["upload_folder"]=upload_folder
basedir=os.path.abspath (os.path.dirname ("/Users/ankitsharma/Desktop/uploadVideo"))
allowed_extensions=set (["txt", "png", "jpg", "jpeg", "xls", "jpg", "png", "xlsx", "gif", "gif", "mp4"])

#Used to determine the file suffix
def allowed_file (filename):
 return "." in filename and filename.rsplit (".", 1) [1] in allowed_extensions
#upload files

@ app.route ("/api/upload", methods=["POST"], strict_slashes=False)
def api_upload ():
 file_dir=os.path.join (basedir, app.config ["upload_folder"])
 
 if not os.path.exists (file_dir):
  os.makedirs (file_dir)

 #get files from the file field of the form,myfile is the name value of the form
 f=request.files ["myfile"] 
 
 #determine if the file type is allowed to be uploaded
 if f and allowed_file (f.filename):
  fname=f.filename
  print (fname," Uploaded to server")
  ext=fname.rsplit (".", 1) [1] #Get file suffix
  unix_time=int (time.time ())
  new_filename=str (unix_time) + "." + ext #changed the uploaded file name
  f.save (os.path.join (file_dir, fname)) #save the file to the upload directory
  # return jsonify ({"errno":0, "errmsg":"Upload succeeded", "token":"Successful"})
  return ("Uploaded !! File name : \n"+fname)
 else:
  return jsonify ({"errno":1001, "errmsg":"Upload failed"})

@app.route('/', methods=['GET'])
def handle_get():
    return "GET msg mac m1"

app.run (host="0.0.0.0", port=5000,debug=True)
