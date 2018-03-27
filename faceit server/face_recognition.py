import requests
import operator
from PIL import Image


class Face():
    def __init__(self, data_face):
        subscription_key = "dbd006b17c8746afabda6ddd9be4f494"
        assert subscription_key
        self.face_api_url = 'https://westcentralus.api.cognitive.microsoft.com/face/v1.0/detect'
        self.headers = {'Ocp-Apim-Subscription-Key': subscription_key, "Content-Type": "application/octet-stream"}

        self.params = {
            'returnFaceId': 'false',
            'returnFaceLandmarks': 'false',
            'returnFaceAttributes': 'age,gender,headPose,smile,facialHair,glasses,'
                                    'emotion,hair,makeup,occlusion,accessories,blur,exposure,noise',
        }
        f = open("image.jpg", "wb")
        f.write(data_face)
        f.close()
        img = Image.open("image.jpg")
        img2 = img.rotate(90)  # rotate
        img2.save("img2.jpg")
        file_data = open("img2.jpg", "rb")
        face_data = file_data.read()
        file_data.close()
        self.data_face = face_data

    def recognition(self):
        response = requests.post(self.face_api_url, params=self.params, headers=self.headers, data=self.data_face)
        faces = response.json()
        if len(faces) > 1:
            return "False: there is more than one face"
        else:
            t_face = faces[0]["faceAttributes"]["emotion"]
            sorted_face = sorted(t_face.items(), key=operator.itemgetter(1), reverse=True)
            return sorted_face[0][0]