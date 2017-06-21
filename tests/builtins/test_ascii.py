from .. utils import TranspileTestCase, BuiltinFunctionTestCase


class AsciiTests(TranspileTestCase):
    pass


class BuiltinAsciiFunctionTests(BuiltinFunctionTestCase, TranspileTestCase):
    functions = ["ascii"]

    not_implemented = [
        'test_class',
        'test_complex',
        'test_slice',
    ]
