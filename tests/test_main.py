import requests

applicationCreateApi = 'http://localhost:8081/applications/'

medicCreateApi = 'http://localhost:8083/medic/save/all'

applicationTreatmentApi = 'http://localhost:8081/applications/assignTreatment'

applicationFinishApi = 'http://localhost:8081/applications/finish'

paymentFinishApi = 'http://localhost:8085/payments/markPaid'

applicationsFindAllApi = 'http://localhost:8081/applications/byAccount'

applicationId = ''


def test_create_medic():
    session = requests.Session()
    headers = {"Content-Type": "application/json; charset=utf-8",
               "user": "c86731c4-6bea-4452-984d-5ba0796760fe",
               "roles": '{roles: [\"patient\"]}'
               }
    data = [{
        "id": "02660d23-8bb0-4618-a3bc-a917ca96bbd9",
        "accountId": "02660d23-8bb0-4528-a3bc-a917ca96bbd9",
        "fio": "Grant Medical",
        "availableTimeList": [
            "2023-04-04T18:05:02Z",
            "2023-05-04T18:05:02Z",
            "2023-06-04T18:05:02Z",
            "2023-07-04T18:05:02Z"
        ]
    }]
    response = session.post(medicCreateApi, headers=headers, json=data, verify=False)
    assert response.status_code == 200


# def test_application_creation():
#     session = requests.Session()
#     headers = {"Content-Type": "application/json; charset=utf-8",
#                "user": "c86731c4-6bea-4452-984d-5ba0796760fe",
#                "roles": '{roles: [\"patient\"]}'
#                }
#     data = {
#         "medicId": "02660d23-8bb0-4618-a3bc-a917ca96bbd9",
#         "type": "LOBOTOMY",
#         "appointmentDate": "2023-04-04T18:05:02Z"
#     }
#     response = session.post(applicationCreateApi, headers=headers, json=data, verify=False)
#     global applicationId
#     applicationId = response.json().get('id')
#     assert response.status_code == 200
#
#
# def test_application_treatment():
#     session = requests.Session()
#     headers = {"Content-Type": "application/json; charset=utf-8",
#                "user": "02660d23-8bb0-4618-a3bc-a917ca96bbd9",
#                "roles": '{roles: [\"medic\"]}'
#                }
#     data = {
#         "id": applicationId,
#         "diagnosisComment": "Беда с бошкой",
#         "treatmentComment": "Лечи голову молотком",
#         "directionComment": "Дмитровское шоссе дом.32 Лечащий врач Психиатр Денис Антонов",
#         "nextAppointmentDate": "2023-06-04T18:05:02Z"
#     }
#     response = session.post(applicationTreatmentApi, headers=headers, json=data, verify=False)
#     assert response.status_code == 200
#     assert response.json().get('status') == "WAITING_FOR_REVISIT"
#     assert response.json().get('treatmentComment') == "Лечи голову молотком"
#     assert response.json().get('diagnosisComment') == "Беда с бошкой"
#
#
# def test_application_finish():
#     session = requests.Session()
#
#     headers = {"Content-Type": "application/json; charset=utf-8",
#                "user": "02660d23-8bb0-4618-a3bc-a917ca96bbd9",
#                "roles": '{roles: [\"medic\"]}'
#                }
#
#     response = session.post(f'{applicationFinishApi}/{applicationId}', headers=headers, verify=False)
#     assert response.status_code == 200
#     assert response.json().get('status') == "WAITING_FOR_PAYMENT"
#     assert response.json().get('paymentId') is not None


# def test_pay_for_application():
#     session = requests.Session()
#     headers = {"Content-Type": "application/json; charset=utf-8",
#                "user": "c86731c4-6bea-4452-984d-5ba0796760fe",
#                "roles": '{roles: [\"patient\"]}'
#                }
#
#     response = session.post(f'{paymentFinishApi}/{applicationId}', headers=headers, verify=False)
#
#     assert response.status_code == 200
#     assert response.json().get('status') is True
#
#     session = requests.Session()
#     response = session.get(applicationsFindAllApi, headers=headers, verify=False)
#     assert response.status_code == 200
#     assert response.json()[0].get('status') == "CLOSED"
