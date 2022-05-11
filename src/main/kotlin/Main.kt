import com.apollographql.apollo3.ast.*
import com.apollographql.apollo3.annotations.ApolloExperimental
import okio.Buffer

val permissionList = hashMapOf<String, Any>(
    "item" to listOf("field_1", "field_2"),
)

val resourceMap = hashMapOf<String, String>(
    "item.item_attribute" to "item_attribute"
)

val graphQLText = """
    query HeroForEpisode(${"$"}ep: Episode) {
      hero(episode: ${"$"}ep) {
        name
        friends {
          height
        }
        foobar
      }
    }
""".trimIndent()


fun main(args: Array<String>) {
    println(permissionList)
    val parseResult = Buffer().writeUtf8(graphQLText).parseAsGQLDocument()
    val queryGqlDocument = parseResult.valueAssertNoErrors()
//    val schemaResult = queryGqlDocument.validateAsSchema()
    val nestedFields = mutableListOf<String>()
    val transformedQuery = queryGqlDocument.transform{ node ->
        if (node is GQLField && node.selectionSet != null) {
            nestedFields.add(node.name)
//            println(node)
//            println(node.name)
        } else if (node is GQLField && node.selectionSet == null) {
            println(node.name)
        }
        TransformResult.Continue
    }
}