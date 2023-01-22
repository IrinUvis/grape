package uvis.irin.grape.core.extension

fun <E> List<E>.withItemAtIndex(item: E, index: Int): List<E> {
    return mapIndexed { itemIndex, previousItem ->
        if (index == itemIndex) item else previousItem
    }
}
