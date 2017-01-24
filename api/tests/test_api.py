from endless import db_session
from endless.main.services import update_user
from models import User


def test_update_user(client, dummy_google_user_info):
    user = User.get(google_id=dummy_google_user_info['sub'])
    assert user is None

    # Test adding Google user to db
    update_user(dummy_google_user_info)
    user = User.get(google_id=dummy_google_user_info['sub'])
    assert user is not None
    assert user.email == dummy_google_user_info['email']
    assert user.full_name == dummy_google_user_info['name']

    # Test updating Google user in db
    new_email = 'dummyuser@dummytest.com'
    user.first_name = 'Second_test'
    user.last_name = 'Dummy_test'
    user.email = new_email
    db_session.commit()
    update_user(dummy_google_user_info)
    user = User.get(google_id=dummy_google_user_info['sub'])
    assert user.full_name == dummy_google_user_info['name']
    assert user.email == new_email
