import requests
import operator
from PIL import Image

class Face():
    def __init__(self, data_face):
        subscription_key = "127b460d0bd149bd9568ac32309dfb6a"
        assert subscription_key
        self.face_api_url = 'https://westcentralus.api.cognitive.microsoft.com/face/v1.0/detect'
        self.headers = {'Ocp-Apim-Subscription-Key': subscription_key, "Content-Type": "application/octet-stream"}

        self.params = {
            'returnFaceId': 'false',
            'returnFaceLandmarks': 'false',
            'returnFaceAttributes': 'age,gender,headPose,smile,facialHair,glasses,'
                                    'emotion,hair,makeup,occlusion,accessories,blur,exposure,noise',
        }
        self.data_face = data_face

    def recognition(self):
        response = requests.post(self.face_api_url, params=self.params, headers=self.headers, data=self.data_face)
        faces = response.json()
        if len(faces) > 1:
            return "False: there is more than one face"
        else:
            t_face = faces[0]["faceAttributes"]["emotion"]
            sorted_face = sorted(t_face.items(), key=operator.itemgetter(1), reverse=True)
            return sorted_face[0][0]

if __name__ == "__main__":
    img = Image.open("image.jpg")
    img2 = img.rotate(180)  # rotate
    img2.save("img2.jpg")
    file_data = open("img2.jpg", "rb")
    face_data = file_data.read()
    file_data.close()
    face = Face(face_data)
    res = face.recognition()
    print res