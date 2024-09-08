# .control - A smart tool supporting melanoma diagnosis
.Control is an application for scoring benigns as malignant or not and therefore supporting early diagnosis of melanoma based on CNN trained on open datasets of ISIC.
## Parts of this project
- Android application (this repository)
- [Code for training the AI model](https://github.com/julia-cieszko/.control-model)
- [Blur detection API](https://github.com/julia-cieszko/.control-blur-api)
- [API serving the model and storing the files and results in the database](https://github.com/julia-cieszko/.control-api)
- Firebase Firestore
- Firebase File Storage
## Motivation
The increasing number of melanoma cases and epidemiological forecasts for the coming decades indicate the need to promote prevention and early diagnosis. Early detection of melanoma is crucial for the patient's survival, which is why the development of preventive and diagnostic tools may lead to a reduction in mortality and the number of cases diagnosed with metastases to other organs. The main task of the application is to support diagnosis by informing the user about the likelihood of melanoma occurrence.
## Capabilities of the app
- Marking possibility of the benign being malignant as high or low
- Provides a poll about user's skin type that helps to identify the risk factors
- Contains readable information on melanoma in a form of FAQ
- Stores additional information on the benign after taking a photo
- Enables to follow changes of each of the added benigns
- Stores user profile data using Firestore
- Provides login using e-mail or Google account
## Used technologies
- Kotlin (application part)
- Firebase Auth, Firestore and Storage
- Python (APIs and model)
- Flask
- Tensorflow
