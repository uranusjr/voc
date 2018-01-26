from .. utils import TranspileTestCase, BuiltinFunctionTestCase


class RoundTests(TranspileTestCase):
    pass


class BuiltinRoundFunctionTests(BuiltinFunctionTestCase, TranspileTestCase):

    functions = ["round"]

    not_implemented = [
        'test_bytearray',
        'test_bytes',
        'test_class',
        'test_complex',
        'test_dict',
        'test_frozenset',
        'test_NotImplemented',
        'test_range',
        'test_set',
        'test_slice',
    ]

    def test_round_int(self):
        self.assertCodeExecution('print(round(42, 0))')
        self.assertCodeExecution('print(round(-42, 0))')
        self.assertCodeExecution('print(round(42, 3))')
        self.assertCodeExecution('print(round(-42, 3))')
        self.assertCodeExecution('print(round(508191992, -8))')
        self.assertCodeExecution('print(round(-508191992, -8))')
