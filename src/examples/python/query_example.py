import graphql

version_query = """
query{
	version{
		appVersion
		fomVersion
	}
}
"""

def main():
    result = graphql.execute_query(version_query)
    print(result)

if __name__ == "__main__":
	main()

