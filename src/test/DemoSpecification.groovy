/*
 *
 *   VÄLKOMNA!
 *
 *                                __
 *    ____________   ____   ____ |  | __
 *   /  ___/\____ \ /  _ \_/ ___\|  |/ /
 *   \___ \ |  |_> >  <_> )  \___|    <
 *  /____  >|   __/ \____/ \___  >__|_ \
 *       \/ |__|               \/     \/
 *
 *
 *      'Test and Specification Framework'
 *
 */


















/*
 *  GRUNDLÄGGANDE TERMINOLOGI:
 *
 *  - En Specifikation är en textmassa som beskriver hur
 *  ett Systemet ska fungera
 *
 *  - Systemet kan vara allt från en klass till en applikation
 *
 *  - För att beskriva systemet behöver vi dess sammanhang, dess kollaboratörer
 *
 *  (- Kollaboratörer tillsammans med Systemet kallas fixtur)
 *
 *  - Specifikationen beskriver ett Systemets features
 */




















/*
 *  I SPOCKS VÄRLD:
 *
 *  - En Specifikation är kod
 *
 *  - Systemet kan vara allt från en klass till en applikation
 *
 *  - Kollaboratörer kan vara riktiga obkjet eller mocks (stubbar och spioner)
 *
 *  - Features beskrivs av featuremetoder
 *
 */














/*
 *  SPOCK IMPLEMENTATION:
 *
 *  - Skrivet i Groovy
 *
 *  - Går att använda för all möjlig kod som kör på JVM:en
 *
 *  - Implementerar en JUnit Runner (Sputnik)
 *
 *  För att få tillgång till bibilioteket...
 *
 */
import spock.lang.*
















/*
 *  För att börja skriva vår Specification
 */
class CinnoberSpecification extends Specification {

























    /*
     *      En Featuremetod
     */
    def 'En feature metod'() {


        /*
         *      Spock använder sig av olika block för att implementera
         *      dom olika konceptuella faserna i en featurespecifikation
         */

//        given:
//        println 'Set up Tests and Collaborators'

//        when:
//        println 'Do stimulus'

//        then:
//        println 'Check conditions and'

    }
















    /*
     *      Enkelt exempel
     *
     */
    def 'Pushes an element on to the stack'() {
        given:
        String elem = 'Cinnober'
        Stack stack = new Stack()

        when:
        stack.push(elem)

        then:
        stack.size() == 1
//        !stack.empty
//        stack.peek() == 'Cinnober'
    }














    /*
     *      Exceptions del 1
     *
     */
    def 'Cant pop empty stack'() {
        given:
        Stack stack = new Stack()

        when:
        stack.pop()

        then:
        thrown(EmptyStackException)
        stack.empty
    }















    /*
     *      Exceptions del 2
     *
     */
    def 'Cant pop empty stack, has null cause'() {
        given:
        Stack stack = new Stack()

        when:
        stack.pop()

        then:
        EmptyStackException e = thrown()
        e.cause == null
    }

















    /*
     *      Exceptions del 3
     *
     */
    def 'HashMap accepts null key'() {
        setup:
        def map = new HashMap()

        when:
        map.put(null, 'elem')

        then:
        notThrown(NullPointerException)
    }



















    /*
     *      Klass att skriva specifikation för
     */
    class Calculator {
        def max(int a, int b){
            Math.max(a, b)
        }
    }























    /*
     *      Expect block
     *
     */
    def 'Max of 1 and 2 is 2'() {
        given:
        def calculator = new Calculator()

        expect:
        calculator.max(1,2) == 2
    }























    /*
     *      Parametrisering
     *
     */
    def 'Max values can be calculated'() {
        given:
        def calculator = new Calculator()

        expect:
        calculator.max(a,b) == c

        where:
        a << [1, 4  ,6]
        b << [3, 26 ,2]
        c << [3, 26 ,6]
    }












    /*
     *      Cleanup block
     *
     */
    def 'File exists'() {
        given:
        File file = File.createTempFile('cinnober', 'txt')

        when:
        file.createNewFile()

        then:
        file.exists()

        cleanup:
        file.delete()
    }














    /*
     *      And block
     *
     */
    def 'File can contain text'() {
        given:
        File file = File.createTempFile('cinnober', 'txt')

        and:
        def text = 'spännande text'

        when:
        file.write text

        then:
        file.text == text

        cleanup:
        file.delete()
    }










    /*
     *      Dokumentation
     *
     */
    def 'Cleanup example with documentations'() {
        given: 'En filreferens'
        File file = File.createTempFile('opkoko', 'txt')

        when: 'vi skapar filen'
        file.createNewFile()

        then: 'då ska den existera'
        file.exists()

        cleanup:
        file.delete()
    }














    /*
     *      Fält i en testklass
     *
     */
    Stack sharedStack = new Stack()

    def 'One feature method'() {
        expect:
        sharedStack.isEmpty()

        when:
        sharedStack.push('opkoko')

        then:
        !sharedStack.isEmpty()
    }

    def 'Second feature method'() {
        expect:
        sharedStack.isEmpty()
    }











    /*
     *      INTERAKTIONER del 1!
     *
     *      Klass att skriva specifikation för
     *
     */
    class Publisher {

        List<Subscriber> subscribers = []

        def send(String message){
            subscribers*.receive(message)
        }
    }

    /*
     *      Kollaboratör
     */
    interface Subscriber {
        void receive(String message)
    }






















    /*
     *      Instansierar vår klass att skriva
     *      specifikation för
     *
     */
    Publisher publisher = new Publisher()

    /*
     *      Vi mockar upp våra kollaboratörer
     *
     */
    Subscriber subscriber = Mock()
    Subscriber subscriber2 = Mock()
    Subscriber subscriber3 = Mock()

    /*
     *       Vi använder oss av Spocks setupmetod
     *
     *       Funkar som JUnits @BeforeClass
     */
    def setup(){
        publisher.subscribers << subscriber
        publisher.subscribers << subscriber2
        // Lägger inte till subscriber 3
    }



















    /*
     *       Testar interaktion mellan publisher
     *       och subscribers
     *
     */
    def 'Send messages to all subscribers'() {
        when:
        publisher.send('hello')

        then:
        1 * subscriber.receive('hello')
        1 * subscriber2.receive('hello')
        0 * subscriber3.receive('hello')
    }












    /*
     *       Testar interaktion mellan publisher
     *       och subscribers
     *
     */
    def 'Send messages to all subscribers in correct order'() {
        when:
        publisher.send('hello')

        then:
        1 * subscriber.receive('hello')

        then:
        1 * subscriber2.receive('hello')
    }























    /*
     *       Testar antalet interaktioner del 1
     *
     */
    def 'Send messages to subscribers at least 1 time'() {
        when:
        publisher.send('hello')

        then:
        (1.._) * subscriber.receive('hello')
    }
















    /*
     *       Testar antalet interaktioner del 2
     *
     */
    def 'Send messages to subscribers max 4 times'() {
        when:
        publisher.send('hello')

        then:
        (_..4) * subscriber.receive('hello')
    }

















    /*
     *       Testa olika typer av begränsningar på argument
     *
     */
    def 'Test different kinds of arguments'() {
        when:
        publisher.send('hello')

        then:
        1 * subscriber.receive('hello')
//        1 * subscriber.receive(!'Mahahaha')
//        1 * subscriber.receive(_)
//        1 * subscriber.receive(!null)
//        1 * subscriber.receive(_ as String)
//        1 * subscriber.receive({ it.size() > 3 })
    }

















    /*
     *      INTERAKTIONER del 2!
     *
     *      Klass att skriva specifikation för
     *
     */
    class SwedishPerson {
        String eatsFromA(SharedPlateOfFood plate){
            if (plate.isAlmostEmpty()){
                return "Hmm... I'm not hungry"
            }

            if(plate.contains('Pizza')){
                plate.eat()
                return 'Yay!'
            } else {
                return 'I only eat italian'
            }
        }
    }

    /*
     *      Kollaboratör
     */
    interface SharedPlateOfFood {
        boolean contains(String ingredient)
        void eat()
        boolean isAlmostEmpty()
    }

















    /*
     *      Vi sätter upp våra objekt
     */
    SwedishPerson swedishPerson = new SwedishPerson()
    SharedPlateOfFood sharedPlate = Mock()





















    /*
     *      Mockar beteende hos tallriken, del 1
     */
    def 'Happily eats food if it contains pizza'(){
        given:
        sharedPlate.contains('Pizza') >> true

        when:
        String response = swedishPerson.eatsFromA(sharedPlate)

        then:
        response == 'Yay!'
        1 * sharedPlate.eat()
    }
















    /*
     *      Mockar beteende hos tallriken, del 2
     */
    def 'Denies non italian food'(){
        given:
        sharedPlate.contains('Pizza') >> false

        when:
        String response = swedishPerson.eatsFromA(sharedPlate)

        then:
        response == 'I only eat italian'
        0 * sharedPlate.eat()
    }
















    /*
     *      Mockar beteende hos tallriken, del 3
     */
    def "Leaves 'svenskbiten' for someone else"(){
        given:
        sharedPlate.isAlmostEmpty() >> true

        when:
        String response = swedishPerson.eatsFromA(sharedPlate)

        then:
        response == "Hmm... I'm not hungry"
        0 * sharedPlate.eat()
    }














    /*
     *      Mockar beteende hos tallriken, del 4
     */
    def "Doesn't eat if something goes wrong"(){
        given:
        sharedPlate.isAlmostEmpty() >> {throw new Exception('Someone stole the food')}

        when:
        String response = swedishPerson.eatsFromA(sharedPlate)

        then:
        thrown(Exception)
        0 * sharedPlate.eat()
    }
















    /*
     *      Mockar beteende hos tallriken, del 5
     */
    def "Eats until 'svenskbiten' is left"(){
        given:
        sharedPlate.contains('Pizza') >> true
        sharedPlate.isAlmostEmpty() >>> [false,false,true]

        when:
        swedishPerson.eatsFromA(sharedPlate)
        swedishPerson.eatsFromA(sharedPlate)
        String response = swedishPerson.eatsFromA(sharedPlate)

        then:
        response == "Hmm... I'm not hungry"
        2 * sharedPlate.eat()
    }
















    /*
     *      Mockar beteende hos tallriken, del 6
     */
    def 'Eats until something goes wrong'(){
        given:
        sharedPlate.contains('Pizza') >> true
        sharedPlate.isAlmostEmpty() >>> [false,false,false] >> {throw new Exception('Someone stole the food')}

        when:
        swedishPerson.eatsFromA(sharedPlate)
        swedishPerson.eatsFromA(sharedPlate)
        swedishPerson.eatsFromA(sharedPlate)
        swedishPerson.eatsFromA(sharedPlate)

        then:
        thrown(Exception)
        3 * sharedPlate.eat()
    }



















    /*
     *      Det finns också Spioner
     */
    SharedPlateOfFood spy = Spy(SharedPlateOfFood)






















}


/*


    End of Demo!

      __                 __
    _/  |______    ____ |  | __
    \   __\__  \ _/ ___\|  |/ /
    |  |  / __ \\  \___|    <
    |__| (____  /\___  >__|_ \
              \/     \/     \/

*/















