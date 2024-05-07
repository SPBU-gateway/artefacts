import unittest
from policies import check_operation


class TestPolicies(unittest.TestCase):
    true_keys = ['default', 'new-device']
    true_headers = [{'from': 'client-auth', 'to': 'manager-input'}, {'from': 'main-manager-output', 'to': 'verifier'}]
    
    def test_keys(self):
        # Testing that only keys "default" and "new-device" are acceptable
        fake_keys = ['asd', '123', 'def', None]
        
        for k in fake_keys:
            self.assertEqual(check_operation(fake_keys, self.true_headers), False)
        for h in self.true_headers:
            self.assertEqual(check_operation('default', h), True)
        
    def test_headers(self):
        # Testing that headers content is checked
        fake_headers = [{'asd': 'asd'}, {}, {'from': 'client-auth'}, {'to': 'manager-input'}]
        for h in fake_headers:
            self.assertEqual(check_operation(self.true_keys[0], h), False)
        
        for k in self.true_keys:
            for h in self.true_headers:
                self.assertEqual(check_operation(k, h), True)
        
    def test_policy(self):
        for k in self.true_keys:
            for h in self.true_headers:
                self.assertEqual(check_operation(k, h), True)
        
        # True expected
        self.assertEqual(check_operation('default', {'from': 'manager-output', 'to': 'storage'}), True)
        self.assertEqual(check_operation('default', {'from': 'client-auth', 'to': 'manager-input'}), True)
        self.assertEqual(check_operation('new-device', {'from': 'client-auth', 'to': 'manager-input'}), True)
        self.assertEqual(check_operation('default', {'from': 'main-hub', 'to': 'main-storage'}), True)
        self.assertEqual(check_operation('default', {'from': 'main-storage', 'to': 'main-manager-output'}), True)
        self.assertEqual(check_operation('default', {'from': 'verifier', 'to': 'device-storage'}), True)
        self.assertEqual(check_operation('new-device', {'from': 'verifier', 'to': 'device-storage'}), True)
        self.assertEqual(check_operation('default', {'from': 'manager-input', 'to': 'client-auth'}), True)
        self.assertEqual(check_operation('new-device', {'from': 'manager-input', 'to': 'client-auth'}), True)
        
        # False expected
        self.assertEqual(check_operation('default', {'from': 'manager-input', 'to': 'device-storage'}), False)
        self.assertEqual(check_operation('new-device', {'from': 'manager-input', 'to': 'storage'}), False)
        self.assertEqual(check_operation('default', {'from': 'verifier', 'to': 'storage'}), False)
        self.assertEqual(check_operation('new-device', {'from': 'verifier', 'to': 'storage'}), False)
        self.assertEqual(check_operation('default', {'from': 'manager-output', 'to': 'main-storage'}), False)
        self.assertEqual(check_operation('default', {'from': 'client-auth', 'to': 'manager-output'}), False)
        self.assertEqual(check_operation('new-device', {'from': 'client-auth', 'to': 'manager-output'}), False)
        self.assertEqual(check_operation('default', {'from': 'main-hub', 'to': 'storage'}), False)
        self.assertEqual(check_operation('default', {'from': 'main-storage', 'to': 'manager-output'}), False)
        
        
if __name__ == '__main__':
    unittest.main()
    