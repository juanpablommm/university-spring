package com.udemy.spring.cource.universityjava.controller;


import com.udemy.spring.cource.universityjava.entity.Person;
import com.udemy.spring.cource.universityjava.service.IPersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Slf4j
@Controller("/")
public class FirstController {

    private IPersonService service;

    /*injectamos la dependencia por medio del controlador y no de la anotacion @Autowired*/
    public FirstController(IPersonService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String index(Model model){
        log.debug("ingresamos al end-point del index.estamos jecutando un controlador Sprign MVC");
        List<Person> people = service.listPeople();
        model.addAttribute("people", people);
        return "index";
    }

    /*al dentrar a ejecutarse el siguiente end-point desde el llamado del index.html
    * estaremos creando un objecto de tipo persona con ayuda de inyeccion de dependencias de spring,
    * podemos hacer uso tambien de un parametro model, y con la misma ayuda de la inyeccion de dependencias
    * le pasamos nuestro ocjeto persona para enviarlo a la template, o tambien podemos relizar esto sin ayuda del
    * objecto model, pero de esta forma al asociar nuestro formulario con el objecto que stamos enviado nos maracara un error
    * para hacer referencia a los atributos de ese objecto que asociemos con el formulario al utlizar las etiquetas th:field
    * de thymele y la sintaxis *{}
    * El obecjto persona al no estar en la instancia en memoria tendremos un obejcto totalmente limpio con los valores de sus
    * atributos en null, pero esa es la idea para que desde la template y con ayuda del la etiqueta th:objecto y th:field
    * al ser asociado al formulario empesemos modificar los valores de sus atributo con ayuda de las etieuqta input de html*/
    @GetMapping("/add")
    public String add(Model model, Person person){
        model.addAttribute(person);
        return "update";
    }

    /*cuando nuestro formulario desde la template modifique y envie el obejto person que recibio desde el end-point /add,
    * y haga el action hacia este end-point estaremos recibiendo el objecto person que contruimos desde dicha template,
    * en este caso spring ya no usara la inyecion de dependencias para instanciar un nuevo objecto person sino que estara tomando la
    * la referencia en memroai del que venimos trabjando y contruimos en la template, podiendo nostros tener ya el objecto en
    * completo para poder aplicar el method save y guardar en la base de datos*/
    @PostMapping("/save")
    public String save(Person person){
        log.info("this is the object person at controller save {}", person);
        this.service.savePerson(person);
        return "redirect:/";
        /*al especificar en el string a retornar la palabra redirec: mas el path donde queremos
        * ir, estaremos diciendole a spring que valla al end-point que espeficamos en path,
        * por ejemplo aka estaremos volviendo a llamar al end-point / que escucha bajo el vervo get,
        * , de esta manera no estamos diciendole que nos dirija directamente hacia la template del index,
        * y como esta en su logica que definimos con la tecnologia de thymeaf esta esperando un lista
        * pues no enviaremos esta en vacia o null, dadnoq ue este controllador no teien definida una lista
        * sino que estara entrando al end-point que si sabra como aplicar esta logica el cual llama
        * al metodo findAll y trae todos los registros para ser enviados a la template*/
    }

    /* añ especificar el path tambien debemos especificar el nombre de la varaible que vien desde la template por
    * path param, el cual tiene que ser tal y como esta definido en el atributo de nuestra clase de domain, esto debe de ir entre llaves,
    * esto es con el fin que se inicilice el parametro de tipo Person uqe tiene nuestro end-point, este solo se inicilizara con el id
    * del objeto Person que viene desde la vista, dado que solo estamos psando esete dato a travez de path param, es como si cojieramos
    * el parametro que tiene nuestro end-point y atravez del method setter para el id le setiaramos el id que recivimos desde la vista
    * buscamos en la base de datos el registro con ese id para ese registro con ayuda de nuestro service
    * y finalmente agregamos el objeto persona ya con todos sus atributos al model para ser enviado a nuestra template de update,
    * en deonde con ayuda de las etiquetas th:object para sociar nuestro objeto person que enviemos desde nuestro end-poin para
    * poder autlizar sus atributos con ayuda de los input y de las etiquetas th:field y finalmente envira este objeto ya armado
    * y con su atributos atulizados al en-poind que finalmente ahra la actulizacion en la base de datos, el cual sera el mismo que ya tenmos
    * configurado en el action del formulario, el end-point en el cual se realiza un save contra la base de datos, ya que como estaremos enviado
    * el objeto person con el id pues al relizar con un id que viene en el objeto un save estaremos actulizando los datos de ese registro mas no guardando uno nuevo,
    * esto debido que jpa con la implmentacion de hibernate de manera inteligente se dara cuenta de esto y relizara un update sobre ese registro en la base
    * de datos y no un nuevo insert*/
    @GetMapping("/update/{id}")
    public String update(Model model, Person person){
        log.info("this is the objet person from controlller update {}", person);
        person = this.service.findPerson(person);
        model.addAttribute(person);
        return "update";
    }
}
