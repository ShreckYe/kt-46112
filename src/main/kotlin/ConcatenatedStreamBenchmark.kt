package benchmark

import kotlinx.benchmark.*
import org.openjdk.jmh.annotations.Fork
import java.util.concurrent.TimeUnit
import java.util.stream.Stream

operator fun <T> Stream<T>.plus(other: Stream<T>) =
    Stream.concat(this, other)

// see: https://github.com/Kotlin/kotlinx-benchmark/blob/master/examples/kotlin-multiplatform/src/jvmMain/kotlin/JvmTestBenchmark.kt
@State(Scope.Benchmark)
@Fork(1)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 2, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 2, time = 1, timeUnit = TimeUnit.SECONDS)
class ConcatenatedStreamBenchmark {
    private lateinit var prependedStream10: Stream<Unit>
    private lateinit var prependedStream100: Stream<Unit>
    private lateinit var prependedStream1000: Stream<Unit>
    private lateinit var appendedStream10: Stream<Unit>
    private lateinit var appendedStream100: Stream<Unit>
    private lateinit var appendedStream1000: Stream<Unit>

    fun prependedStream(length: Int): Stream<Unit> =
        if (length == 0) Stream.empty()
        else Stream.of(Unit) + prependedStream(length - 1)

    fun appendedStream(length: Int): Stream<Unit> =
        if (length == 0) Stream.empty()
        else prependedStream(length - 1) + Stream.of(Unit)

    @Setup
    fun setup() {
        prependedStream10 = prependedStream(10)
        prependedStream100 = prependedStream(100)
        prependedStream1000 = prependedStream(1000)
        appendedStream10 = appendedStream(10)
        appendedStream100 = appendedStream(100)
        appendedStream1000 = appendedStream(1000)
    }

    @Benchmark
    fun prependedStream10Benchmark() = prependedStream10.forEach { }

    @Benchmark
    fun prependedStream100Benchmark() = prependedStream100.forEach { }

    @Benchmark
    fun prependedStream1000Benchmark() = prependedStream1000.forEach { }

    @Benchmark
    fun appendedStream10Benchmark() = appendedStream10.forEach { }

    @Benchmark
    fun appendedStream100Benchmark() = appendedStream100.forEach { }

    @Benchmark
    fun appendedStream1000Benchmark() = appendedStream1000.forEach { }
}