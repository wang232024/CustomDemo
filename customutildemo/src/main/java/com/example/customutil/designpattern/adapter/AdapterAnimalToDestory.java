package com.example.customutil.designpattern.adapter;

public class AdapterAnimalToDestory extends Animal implements IDestroy {
        Animal animal;
        
        public AdapterAnimalToDestory(Animal animal) {
                this.animal = animal;
        }
        
        @Override
        public void destory() {
                // TODO Auto-generated method stub
                if (animal.isFlyable())
                        System.out.println(animal.getClass() + " destory a town");
                else
                        System.out.println(animal.getClass() + " can't destory a town");
        }

}
