import rsa


class RSA():
    """
    RSA Class to implement encrypted communication between
    the client and the server.

    Only the public key is shared through the network (Secure)

    Bob (encrypt with Alice public key) ---> Alice (decrypt with Alice private key)
    Bob (decrypt with Bob private key) <--- Alice (encrypt with Bob public key)
    """
    def __init__(self, listener_private_key, destination_public_key):
        """
        To communicate, the listener only need his private key
        to decrypt the message from the destination.
        In the other hand, he need the destination public key
        to encrypt the data
        :param listener_private_key:
        :param destination_public_key:
        """
        with open(listener_private_key, 'rb') as server_pem:
            self.listener_private_key = rsa.PrivateKey.load_pkcs1(server_pem.read())

        with open(destination_public_key, 'rb') as client_pub:
            self.destination_public_key = rsa.PublicKey.load_pkcs1(client_pub.read())


    def encrypt(self, message):
        """
        Encrypt a message using the destination public key

        :param message:
        :return: Encrypted message
        """
        return rsa.encrypt(message, self.destination_public_key)


    def decrypt(self, message):
        """
        Decrypt a message using the listener private key

        :param message:
        :return: Decrypted message
        """
        return rsa.decrypt(message, self.listener_private_key)