import graphql

join_mutation = """
mutation join{
	joinFederation(federateName:"IDL")
}
"""

def main():
    result = graphql.execute_mutation(join_mutation)
    print(result)

if __name__ == "__main__":
	main()
