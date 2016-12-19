from endless.tools.rsa import RSA


def test_rsa(client):
    # We simulate the communication between client/server
    server = RSA("tests/resources/test_server.pem", "tests/resources/test_client.pub")
    client = RSA("tests/resources/test_client.pem", "tests/resources/test_server.pub")

    assert client.decrypt(server.encrypt(b"Allo mon beau monsieur!")).decode("utf-8") == "Allo mon beau monsieur!"
    assert server.decrypt(client.encrypt(b"T Lette")).decode("utf-8") == "T Lette"
