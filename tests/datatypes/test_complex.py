from .. utils import TranspileTestCase, UnaryOperationTestCase, BinaryOperationTestCase, InplaceOperationTestCase


class ComplexTests(TranspileTestCase):
    def test_conjugate(self):
        self.assertCodeExecution("""
            x = complex(1.5, 1)
            print(x.conjugate())
            """)

    def test_real_imag(self):
        self.assertCodeExecution("""
            x = complex(1, 2.0)
            print(x.real)
            print(x.imag)
            """)

    def test_equality_with_numbers_when_zero_imag(self):
        self.assertCodeExecution("""
            x = 2
            y = complex(2, 0)
            print(x == y)
            print(y.__eq__(x))
            print(x != y)
            print(y.__ne__(x))

            x = 2.0
            y = complex(3, 0)
            print(x == y)
            print(y.__eq__(x))
            print(x != y)
            print(y.__ne__(x))

            x = True
            y = complex(1, 0)
            print(x == y)
            print(y.__eq__(x))
            print(x != y)
            print(y.__ne__(x))
            """)


class UnaryComplexOperationTests(UnaryOperationTestCase, TranspileTestCase):
    data_type = 'complex'


class BinaryComplexOperationTests(BinaryOperationTestCase, TranspileTestCase):
    data_type = 'complex'

    substitutions = {
        "(-161.18751321137705+195.77962956590406j)": [
            "(-161.18751321137705+195.77962956590403j)"
        ],
        "(2.6460893340172016e-18+0.04321391826377225j)": [
            "(2.6460019439688186e-18+0.04321391826377225j)"
        ],
        "(-9.8368221286278e-14-535.4916555247646j)": [
            "(-9.836497256617357e-14-535.4916555247646j)"
        ]
    }

    is_flakey = [
        'test_power_complex',
        'test_power_float',
    ]


class InplaceComplexOperationTests(InplaceOperationTestCase, TranspileTestCase):
    data_type = 'complex'

    not_implemented = [
        'test_multiply_bytearray',
        'test_multiply_bytes',
        'test_multiply_list',
        'test_multiply_str',
        'test_multiply_tuple',
    ]

    substitutions = {
        # Floating point error fix for test_power_XXX.
        "(0.2205799751711092+0.9753688915243878j)": [
            "(0.22057997517110922+0.9753688915243878j)"
        ],
        "(0.7071067811865474+0.7071067811865477j)": [
            "(0.7071067811865475+0.7071067811865477j)"
        ],
        "(0.0060495771063351165+0.026750249254585242j)": [
            "(0.006049577106335116+0.026750249254585242j)"
        ],
        "(0.012866500744683351+0.012866500744683356j)": [
            "(0.012866500744683353+0.012866500744683356j)"
        ],
        "(0.01206055994891249+0.01684497328631129j)": [
            "(0.012060559948912487+0.016844973286311293j)"
        ],
        "(2641.9999999999964+6469.000000000001j)": [
            "(2641.999999999997+6469.000000000001j)"
        ],
        "(0.0014050407093500303-0.006212862242650542j)": [
            "(0.00140504070935003-0.006212862242650542j)"
        ],
        "(0.002529822128134703-0.002529822128134704j)": [
            "(0.0025298221281347035-0.002529822128134704j)"
        ],
        "(0.2291401859804338+0.23817011512167555j)": [
            "(0.22914018598043387+0.23817011512167555j)"
        ],
        "(-0.22251715680177267+0.10070913113607541j)": [
            "(-0.22251715680177267+0.1007091311360754j)"
        ],
        "(-161.18751321137705+195.77962956590406j)": [
            "(-161.18751321137705+195.77962956590403j)"
        ],
        "(-21.083139690689016+24.00021070941257j)": [
            "(-21.083139690689013+24.00021070941257j)"
        ],
        "(-2.997990598421402-0.6237845862790473j)": [
            "(-2.9979905984214024-0.6237845862790473j)"
        ],
    }
