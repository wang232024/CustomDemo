package com.example.customutil.designpattern.adapter;

public class AdapterAnimalToStorm{
        private Animal animal;
        private Storm storm;
        
       public  AdapterAnimalToStorm(Animal animal, Storm storm) {
               this.animal = animal;
               this.storm = storm;
       }

       public void adapter() {
               if (animal.isFlyable())
                       storm.destory(animal);
               else
                       System.out.println(animal.getClass() + " can't destory a town");
       }
       
}
